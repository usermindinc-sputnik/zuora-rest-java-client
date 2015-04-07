/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.lib;

import com.usermind.integrations.common.boot.CommonLib;
import com.usermind.integrations.common.config.Configuration;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class ZAPI {
  private final Logger logger = CommonLib.get().getLoggerFactory().getLogger(getClass());

  private String defaultTenantUserId;
  private String defaultTenantPassword;
  private String connectTenantUserId;
  private String connectTenantPassword;
  private DefaultHttpClient zHttpClient;
  private Configuration configuration;

  public ZAPI(Configuration configuration, String defaultTenantUserId, String defaultTenantPassword) {
    this.defaultTenantUserId = defaultTenantUserId;
    this.defaultTenantPassword = defaultTenantPassword;
    this.configuration = configuration;
  }

  public void setConnectCredentials(String connectTenantUserId, String connectTenantPassword) {
    this.connectTenantUserId = connectTenantUserId;
    this.connectTenantPassword = connectTenantPassword;
  }

  public ZAPIResp execGetAPI(String uri, Map<String, String> queryString) {
    String url;

    // For a nextPage call the uri is the URL
    if (uri.toLowerCase().startsWith("http")) {
      url = uri;
    } else {
      // turn uri to URL
      url = configuration.getString("rest.api.endpoint") +
          "/" + configuration.getString("rest.api.version") + uri;
    }

    // Get a httpget request ready
    HttpGet httpGet = new HttpGet(url);

    // indicate accept response body in JSON
    httpGet.setHeader("Accept", "application/json");

    // for a GET call, chase redirects
    httpGet.setHeader("follow_redirect", "true");

    // build query string into url
    URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
    Iterator<Map.Entry<String, String>> it = queryString.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> pairs = it.next();
      uriBuilder.addParameter(pairs.getKey(), pairs.getValue());
    }

    // perform pre API arguments tracing if required
    logger.debug("***** PRE-API TRACE *****");
    logger.debug("HTTP method = GET");
    logger.debug("URL = " + url);
    logger.debug("Query String = " + queryString.toString());
    Header headers[] = httpGet.getAllHeaders();
    for (Header h : headers) {
      logger.debug("Header = " + h.getName() + ": " + h.getValue());
    }

    // get a ssl pipe (httpclient), execute, trace response
    try {
      httpGet.setURI(uriBuilder.build());
      ZAPIResp resp = tracePostAPIResponse(httpGet, sslPipe().execute(httpGet));
      httpGet.releaseConnection();
      return resp;
    } catch (Exception e) {
      logger.error("Fatal Error in executing HTTP GET " + url, e);
      httpGet.abort();
      throw new RuntimeException("Fatal Error in executing HTTP GET " + url);
    }
  }

  public ZAPIResp execPutAPI(String uri, String reqBody) {
    String url;

    // turn uri to URL
    url = configuration.getString("rest.api.endpoint") +
        "/" + configuration.getString("rest.api.version") + uri;

    // Get a httpput request ready
    HttpPut httpPut = new HttpPut(url);

    // indicate accept response body in JSON
    httpPut.setHeader("Accept", "application/json");

    // For a PUT call, request body content is in JSON
    httpPut.setHeader("Content-Type", "application/json");

    // perform pre API tracing
    logger.debug("***** PRE-API TRACE *****");
    logger.debug("HTTP method = PUT");
    logger.debug("URL = " + url);
    logger.debug("Request Body = " + reqBody.toString());

    Header headers[] = httpPut.getAllHeaders();
    for (Header h : headers) {
      logger.debug("Header = " + h.getName() + ": " + h.getValue());
    }

    // get a ssl pipe (httpclient), execute, trace response
    try {
      StringEntity entity = new StringEntity(reqBody);
      httpPut.setEntity(entity);
      ZAPIResp resp = tracePostAPIResponse(httpPut, sslPipe().execute(httpPut));
      httpPut.releaseConnection();
      return resp;
    } catch (Exception e) {
      logger.error("Fatal Error in executing HTTP PUT " + url, e);
      httpPut.abort();
      throw new RuntimeException("Fatal Error in executing HTTP PUT " + url);
    }
  }

  // Do POST
  public ZAPIResp execPostAPI(String uri, String reqBody) {
    return execPostAPI(uri, reqBody, null);
  }

  // Do POST
  public ZAPIResp execPostAPI(String uri, String reqBody, String reqParams) {
    String url;
    // For POST CONNECT call the version number is not in the url
    if (uri.toLowerCase().contains(ZConstants.CONNECTION_URI)) {
      url = configuration.getString("rest.api.endpoint") + uri;
    } else {
      // turn the resource uri to a full URL
      url = configuration.getString("rest.api.endpoint") +
          "/" + configuration.getString("rest.api.version") + uri;
    }

    // Get a httpput request ready
    HttpPost httpPost = new HttpPost(url);

    // indicate accept response body in JSON
    httpPost.setHeader("Accept", "application/json");

    // For file upload dont need to set content type
    if (!(uri.toLowerCase().contains(ZConstants.UPLOAD_USAGE_URL) || uri.toLowerCase().contains(
        ZConstants.MASS_UPDATER_URL))) {
      // For non-POST USAGE call, request body content is in JSON
      httpPost.setHeader("Content-Type", "application/json");
    }

    // put tenant's credentials in request header for a POST CONNECTION
    if (uri.toLowerCase().contains(ZConstants.CONNECTION_URI)) {
      // put tenant's credentials in request header
      httpPost.setHeader("apiAccessKeyId", tenantUserIdToUse());
      httpPost.setHeader("apiSecretAccessKey", tenantPasswordToUse());
    }

    // perform pre API tracing
    logger.debug("***** PRE-API TRACE *****");
    logger.debug("HTTP method = POST");
    logger.debug("URL = " + url);
    logger.debug("Request Body = " + reqBody.toString());

    Header headers[] = httpPost.getAllHeaders();
    for (Header h : headers) {
      logger.debug("Header = " + h.getName() + ": " + h.getValue());
    }

    // get a ssl pipe (httpclient), execute, trace response
    try {
      if (uri.contains(ZConstants.UPLOAD_USAGE_URL) || uri.contains(ZConstants.MASS_UPDATER_URL)) {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        // For File parameters
        entity.addPart("file", new FileBody(new File(reqBody)));
        if (uri.contains(ZConstants.MASS_UPDATER_URL))
          entity.addPart("params", new StringBody(reqParams));
        httpPost.setEntity(entity);
      } else {
        StringEntity entity = new StringEntity(reqBody);
        httpPost.setEntity(entity);
      }
      ZAPIResp resp = tracePostAPIResponse(httpPost, sslPipe().execute(httpPost));
      httpPost.releaseConnection();
      return resp;
    } catch (Exception e) {
      logger.error("Fatal Error in executing HTTP POST " + url, e);
      httpPost.abort();
      throw new RuntimeException("Fatal Error in executing HTTP POST " + url);
    }
  }

  // Do DELETE
  public ZAPIResp execDeleteAPI(String uri, Map<String, String> queryString) {
    String url;

    // turn uri to URL
    url = configuration.getString("rest.api.endpoint") +
        "/" + configuration.getString("rest.api.version") + uri;

    // Get a httpdelete request ready
    HttpDelete httpDelete = new HttpDelete(url);

    // indicate accept response body in JSON
    httpDelete.setHeader("Accept", "application/json");

    // build query string into url
    URIBuilder uriBuilder = new URIBuilder(httpDelete.getURI());
    Iterator<Map.Entry<String, String>> it = queryString.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> pairs = it.next();
      uriBuilder.addParameter(pairs.getKey(), pairs.getValue());
    }

    // perform pre API arguments tracing if required
    logger.debug("***** PRE-API TRACE *****");
    logger.debug("HTTP method = DELETE");
    logger.debug("URL = " + url);
    logger.debug("Query String = " + queryString.toString());

    Header headers[] = httpDelete.getAllHeaders();
    for (Header h : headers) {
      logger.debug("Header = " + h.getName() + ": " + h.getValue());
    }

    // get a ssl pipe (httpclient), execute, trace response
    try {
      httpDelete.setURI(uriBuilder.build());
      ZAPIResp resp = tracePostAPIResponse(httpDelete, sslPipe().execute(httpDelete));
      httpDelete.releaseConnection();
      return resp;
    } catch (Exception e) {
      logger.error("Fatal Error in executing HTTP DELETE " + url, e);
      httpDelete.abort();
      throw new RuntimeException("Fatal Error in executing HTTP DELETE " + url);
    }
  }

  // Get a SSL pipe for all http traffic
  private DefaultHttpClient sslPipe() {
    if (zHttpClient == null) {
      zHttpClient = ZHttpClient.getInstance(configuration);
    }
    return zHttpClient;
  }

  // resolve final tenant user Id to use
  private String tenantUserIdToUse() {
    if (connectTenantUserId == null) {
      return defaultTenantUserId;
    } else {
      return connectTenantUserId;
    }
  }

  // resolve final tenant password to use
  private String tenantPasswordToUse() {
    if (connectTenantPassword == null) {
      return defaultTenantPassword;
    } else {
      return connectTenantPassword;
    }
  }

  // Print some HTTP artifacts and response
  private ZAPIResp tracePostAPIResponse(HttpUriRequest httpRequest, HttpResponse httpResp)
      throws JSONException {
    JSONObject jsonObjResp = null;
    String jsonResp = null;

    try {
      jsonObjResp = new JSONObject(EntityUtils.toString(httpResp.getEntity()));
      // If there is no JSON response create an empty JSON object
    } catch (Exception e) {
      jsonObjResp = new JSONObject();
    }

    // then add HTTP status and reason inside
    jsonObjResp.put("httpStatusCode", httpResp.getStatusLine().getStatusCode());
    jsonObjResp.put("httpReasonbPhrase", httpResp.getStatusLine().getReasonPhrase());
    jsonResp = jsonObjResp.toString(2);

    logger.debug("***** POST-API RESPONSE TRACE *****");
    logger.debug("HTTP method = " + httpRequest.getMethod());
    logger.debug(
        "Proxy = " + sslPipe().getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY));
    logger.debug("URL = " + httpRequest.getURI().toString());

    Header headers[] = httpResp.getAllHeaders();
    for (Header h : headers) {
      logger.debug("Header = " + h.getName() + ": " + h.getValue());
    }

    logger.debug("HTTP status = " + httpResp.getStatusLine().getStatusCode());
    logger.debug("HTTP reason = " + httpResp.getStatusLine().getReasonPhrase());
    logger.debug("HTTP version = " + httpResp.getProtocolVersion());
    logger.debug("API Response = " + jsonResp);

    // convert json response string to ZAPIResp and return result
    return new ZAPIResp(jsonResp);
  }

}
