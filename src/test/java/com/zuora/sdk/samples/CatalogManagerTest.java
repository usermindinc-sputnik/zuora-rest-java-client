package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class CatalogManagerTest extends BaseZuoraApiTest {
  @Test
  public void test_catalog_manager() {
    // Create a z_client
    ZClient zClient = new ZClient(getConfiguration());

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
