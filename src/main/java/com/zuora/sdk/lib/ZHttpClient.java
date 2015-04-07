/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.lib;

import com.usermind.integrations.common.boot.CommonLib;
import com.usermind.integrations.common.config.Configuration;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;


public class ZHttpClient {

  private final Logger logger = CommonLib.get().getLoggerFactory().getLogger(getClass());

  private static DefaultHttpClient instance_;
  private Configuration configuration;

  // Each thread has its own httpclient
  ZHttpClient(Configuration configuration) {
    this.configuration = configuration;

    // Use DefaultClient for verfiy_peer client
    if (configuration.getBoolean("ssl.verify.peer")) {
      PoolingClientConnectionManager pccm = new PoolingClientConnectionManager();

      // set max connections
      pccm.setMaxTotal(configuration.getInt("http.max.connection.pool.size"));
      pccm.setDefaultMaxPerRoute(configuration.getInt("http.max.connection.pool.size"));
      pccm.closeIdleConnections(15, TimeUnit.SECONDS);
      instance_ = new DefaultHttpClient(pccm);
    } else {
      instance_ = friendlyHttpClient();
    }
    configHttpClient();
  }

  // ZHttpClient is a singleton shared by all ZClients
  public synchronized static DefaultHttpClient getInstance(Configuration configuration) {
    if (instance_ == null) {
      new ZHttpClient(configuration);
    }
    return instance_;
  }

  // Return a verify_none httpclient
  private DefaultHttpClient friendlyHttpClient() {
    try {
      SchemeRegistry registry = new SchemeRegistry();
      // say yes to any cert
      SSLSocketFactory socketFactory = new SSLSocketFactory(new TrustStrategy() {
        @Override
        public boolean isTrusted(final X509Certificate[] chain, String authType)
            throws CertificateException {
          return true;
        }
      }, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      // need both http and https
      registry.register(new Scheme("https", 443, socketFactory));
      registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(registry);

      // configure limits
      pccm.setMaxTotal(configuration.getInt("http.max.connection.pool.size"));
      pccm.setDefaultMaxPerRoute(configuration.getInt("http.max.connection.pool.size"));

      // shut down connection after being idle for a minute
      pccm.closeIdleConnections(60, TimeUnit.SECONDS);

      instance_ = new DefaultHttpClient(pccm);
      return instance_;
    } catch (GeneralSecurityException e) {
      String errorMessage = "Fatal Error in creating friendlyHTTPClient";
      logger.error(errorMessage, e);
      throw new RuntimeException(errorMessage);
    }
  }

  // set all the nuts and bolts for httpclient
  private void configHttpClient() {
    instance_.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    // set timeout parameters
    instance_.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
        configuration.getInt("http.connect.timeout"));
    instance_.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
        configuration.getInt("http.receive.timeout"));

    // get settings for proxy
    boolean proxyUsed = configuration.getBoolean("proxy.used");
    String urlString = configuration.getString("proxy.url");

    // if proxy is used and proxy url is specified ...
    if (proxyUsed && urlString != null && !urlString.equals("")) {
      // decode URL
      try {
        URL url = new URL(urlString);
        String proxyProtocol = url.getProtocol();
        String proxyHost = url.getHost();
        int proxyPort = url.getPort();

        // add authenticating proxy support if in use
        if (configuration.getBoolean("proxy.auth")) {
          instance_.getCredentialsProvider().setCredentials(
              new AuthScope(proxyHost, proxyPort),
              new UsernamePasswordCredentials(configuration.getString("proxy.user"),
                  configuration.getString("proxy.password")));
        }

        // set proxy
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyProtocol);
        instance_.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        // proxy url is malformed ... giving up
      } catch (MalformedURLException e) {
        logger.warn(
            "Unable to use Proxy - proxy URL string {} is malformed. Proxy config is not used.",
            urlString);
      }
    }
    // add Useragent
    instance_.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
        configuration.getString("http.user.agent"));
  }

  // Clean up all TCP connections before being collected
  @Override
  public void finalize() {
    try {
      super.finalize();
    } catch (Throwable ex) {
    }
    instance_.getConnectionManager().shutdown();
  }
}
