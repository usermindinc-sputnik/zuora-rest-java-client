/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to program a multi-threaded application
 */
package com.zuora.sdk.samples;

import com.usermind.integrations.common.config.Configuration;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

public class MultithreadedExecution implements Runnable {
  private Configuration configuration;

  public MultithreadedExecution(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void run() {
    // Create a z_client
    ZClient zClient = new ZClient(configuration);

    // Create a Catalog resource object with a zclient
    CatalogManager catalogManager = new CatalogManager(zClient);

    // Connect to the End Point using default tenant's credentials
    // and practice APIs
    ZAPIResp resp = null;
    if (new ConnectionManager().isConnected(zClient)) {
      resp = catalogManager.getProducts();
    }

    // follow nextPage if present
    while (resp != null) {
      String nextPageLink = (String) resp.get("nextPage");
      if (nextPageLink == null) {
        resp = null;
      } else {
        resp = catalogManager.getProducts(nextPageLink);
      }
    }
  }

}
