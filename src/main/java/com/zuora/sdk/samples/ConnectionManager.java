/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to use the Connections resources
 */

package com.zuora.sdk.samples;

import com.usermind.integrations.common.boot.CommonLib;
import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;
import org.slf4j.Logger;

public class ConnectionManager {

  private final Logger logger = CommonLib.get().getLoggerFactory().getLogger(getClass());
  ZClient zClient;

  public boolean isConnected(ZClient zClient, String apiAccessKeyId, String apiSecretAccessKey) {
    this.zClient = zClient;

    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.CONNECT);
    args.set("headers", new ZAPIArgs());
    args.getArg("headers").set("apiAccessKeyId", apiAccessKeyId);
    args.getArg("headers").set("apiSecretAccessKey", apiSecretAccessKey);

    logger.debug("========== CONNECT SERVICE ENDPOINT ============");

    ZAPIResp response = null;
    try {
      response = zClient.post(args);
      logger.info(response.toJSONString());
      if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
        return true;
      }
    } catch (IllegalArgumentException e) {
      logger.error(e.getMessage(), e);
    } catch (RuntimeException e) {
      logger.error(e.getMessage(), e);
    }

    return false;
  }

  public boolean isConnected(ZClient zClient) {
    return isConnected(zClient, null, null);
  }
}
