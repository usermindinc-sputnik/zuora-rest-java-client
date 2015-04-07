/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.lib;

import com.usermind.integrations.common.boot.CommonLib;
import com.usermind.integrations.common.config.Configuration;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ZClient {

  private final Logger logger = CommonLib.get().getLoggerFactory().getLogger(getClass());

  private String defaultTenantUserId;
  private String defaultTenantPassword;
  private String connectTenantUserId;
  private String connectTenantPassword;
  private ZAPI zAPI;
  private Configuration configuration;

  public ZClient(Configuration configuration) {
    this.configuration = configuration;

    // Use default tenant credentials in config.yml if present
    defaultTenantUserId = configuration.getString("default.tenant.user.id");
    defaultTenantPassword = configuration.getString("default.tenant.password");

    // Z_API is the api handler for this z_client
    zAPI = new ZAPI(configuration, defaultTenantUserId, defaultTenantPassword);
  }

  // Validate and process a REST GET API
  // arguments are in z_api_args
  public ZAPIResp get(ZAPIArgs zAPIArgs) {
    // validate resource URI
    String errorMsg = isBadURI((String) zAPIArgs.get("uri"));
    if (errorMsg != null) {
      throw new IllegalArgumentException(errorMsg);
    }

    // For GET, reject if request_body is present
    ZAPIArgs requestBody = zAPIArgs.getArg("reqBody");
    if (requestBody != null) {
      errorMsg = "Extraneous request body argument " + requestBody.toString() + " found.";
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg);
    }

    // verify tenant's credential existence
    errorMsg = doesCredentialsExist();
    if (errorMsg != null) {
      throw new RuntimeException(errorMsg);
    }

    // may or may not have query string
    Map<String, String> queryString = new HashMap<String, String>();

    ZAPIArgs queryNVPS = zAPIArgs.getArg("queryString");
    if (queryNVPS != null) {
      Iterator<?> keys = queryNVPS.keys();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        queryString.put(key, String.valueOf(queryNVPS.get(key)));
      }
    }

    try {
      return zAPI.execGetAPI((String) zAPIArgs.get("uri"), queryString);
    } catch (Exception e) {
      logger.error("HTTP GET Exception. Please see logs for details.", e);
      throw new RuntimeException("HTTP GET Exception. Please see logs for details.");
    }
  }

  // Validate and process a REST POST API
  // arguments are in z_api_args
  public ZAPIResp post(ZAPIArgs zAPIArgs) {
    String uri = (String) zAPIArgs.get("uri");

    // validate resource URI
    String errorMsg = isBadURI(uri);
    if (errorMsg != null) {
      throw new IllegalArgumentException(errorMsg);
    }

    // For POST, reject if query string is present
    ZAPIArgs queryString = zAPIArgs.getArg("queryString");
    if (queryString != null) {
      errorMsg = "Extraneous query string argument " + queryString.toString() + " found.";
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg);
    }

    ZAPIArgs reqBody = zAPIArgs.getArg("reqBody");

    // For POST CONNECTION, reject if request_body is present
    if (uri.contains(ZConstants.CONNECTION_URI)) {
      if (reqBody != null) {
        errorMsg = "Extraneous request body argument " + reqBody.toString() + " found.";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      } else {
        // For POST CONNECTION, extract target tenant user id and password
        connectTenantUserId = (String) zAPIArgs.getArg("headers").get("apiAccessKeyId");
        connectTenantPassword = (String) zAPIArgs.getArg("headers").get("apiSecretAccessKey");

        // override the default credentials of z_api when created
        zAPI.setConnectCredentials(connectTenantUserId, connectTenantPassword);
        reqBody = new ZAPIArgs();
      }
    } else {
      // Other than POST CONNECTION, reject if request_body is missing
      if (reqBody == null) {
        errorMsg = "Missing request body argument.";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }
    }

    if (uri.contains(ZConstants.UPLOAD_USAGE_URL) || uri.contains(ZConstants.MASS_UPDATER_URL)) {
      if (reqBody.get("filename") == null || reqBody.get("filename").equals("")) {
        errorMsg = "Missing filename argument in request body.";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
      }
    }

    // verify tenant's credential existence
    errorMsg = doesCredentialsExist();
    if (errorMsg != null) {
      throw new RuntimeException(errorMsg);
    }

    try {
      // Take care of an UPLOAD POST
      if (uri.contains(ZConstants.UPLOAD_USAGE_URL)) {
        return zAPI.execPostAPI(uri, (String) reqBody.get("filename"));
        // Just a regular post
      } else if (uri.contains(ZConstants.MASS_UPDATER_URL)) {
        return zAPI.execPostAPI(uri, (String) reqBody.get("filename"),
            (String) zAPIArgs.get("params"));
      } else {
        return zAPI.execPostAPI(uri, reqBody.toJSONString());
      }
    } catch (Exception e) {
      logger.error("HTTP POST Exception. Please see logs for details.", e);
      throw new RuntimeException("HTTP POST Exception. Please see logs for details.");
    }
  }

  // Validate and process a REST PUT API
  public ZAPIResp put(ZAPIArgs zAPIArgs) {
    String uri = (String) zAPIArgs.get("uri");

    // validate the resource URI
    String errorMsg = isBadURI(uri);
    if (errorMsg != null) {
      throw new IllegalArgumentException(errorMsg);
    }

    // For PUT, reject if query string is present
    ZAPIArgs queryString = zAPIArgs.getArg("queryString");
    if (queryString != null) {
      errorMsg = "Extraneous query string argument " + queryString.toString() + " found.";
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg);
    }

    ZAPIArgs reqBody = zAPIArgs.getArg("reqBody");
    // For PUT, reject if request_body is missing
    if (reqBody == null) {
      errorMsg = "Missing request body argument.";
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg);
    }

    // verify tenant's credential existence
    errorMsg = doesCredentialsExist();
    if (errorMsg != null) {
      throw new RuntimeException(errorMsg);
    }

    try {
      return zAPI.execPutAPI(uri, reqBody.toJSONString());
    } catch (Exception e) {
      logger.error("HTTP PUT Exception. Please see logs for details.", e);
      throw new RuntimeException("HTTP PUT Exception. Please see logs for details.");
    }
  }

  // Validate and process a REST DELETE API call
  public ZAPIResp delete(ZAPIArgs zAPIArgs) {
    String uri = (String) zAPIArgs.get("uri");

    // validate resource URI
    String errorMsg = isBadURI(uri);
    if (errorMsg != null) {
      throw new IllegalArgumentException(errorMsg);
    }

    // For DELETE, reject if request_body is present
    ZAPIArgs reqBody = zAPIArgs.getArg("reqBody");
    if (reqBody != null) {
      errorMsg = "Extraneous request body argument " + reqBody.toString() + " found.";
      logger.error(errorMsg);
      throw new IllegalArgumentException(errorMsg);
    }

    // verify tenant's credential existence
    errorMsg = doesCredentialsExist();
    if (errorMsg != null) {
      throw new RuntimeException(errorMsg);
    }

    // may or may not have query string
    Map<String, String> queryString = new HashMap<>();

    ZAPIArgs queryNVPS = zAPIArgs.getArg("queryString");
    if (queryNVPS != null) {
      Iterator<?> keys = queryNVPS.keys();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        queryString.put(key, (String) queryNVPS.get(key));
      }
    }

    try {
      return zAPI.execDeleteAPI(uri, queryString);
    } catch (Exception e) {
      logger.error("HTTP DELETE Exception. Please see logs for details.", e);
      throw new RuntimeException("HTTP DELETE Exception. Please see logs for details.");
    }
  }

  // perform sanity check on a resource uri
  private String isBadURI(String uri) {
    String errorMsg = null;

    // Reject if missing uri
    if (uri == null) {
      errorMsg = "URI is missing.";
      logger.error(errorMsg);
      return errorMsg;
    }

    // Reject if incorrect uri after sanity check
    URL url = null;
    try {
      url = new URL(configuration.getString("rest.api.endpoint") + "/" +
          configuration.getString("rest.api.version") + uri);
    } catch (MalformedURLException e) {
      errorMsg = "URI " + url + " is an incorrect URL.";
      logger.error(errorMsg);
    }
    return errorMsg;
  }

  // Verify if there were any tenant's credentials that can be used
  // while processing a REST API call
  private String doesCredentialsExist() {
    String errorMsg = null;

    // reject if both default and connect credentials are missing
    if ((defaultTenantUserId == null || defaultTenantUserId.equals("") ||
        defaultTenantPassword == null || defaultTenantPassword.equals("")) &&
        (connectTenantUserId == null || connectTenantUserId.equals("") ||
            connectTenantPassword == null || connectTenantPassword.equals(""))) {
      errorMsg =
          "Unable to establish valid tenant's credentials in a CONNECT call or configuration.";
      logger.error(errorMsg);
    }
    return errorMsg;
  }
}
