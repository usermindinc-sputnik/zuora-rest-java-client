package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class UsageManagerTest extends BaseZuoraApiTest {
  static final String SAMPLE_ACCOUNT_KEY = "A00001069";
  static final String SAMPLE_USAGE_FILE = UsageManager.class.getClassLoader()
      .getResource("com/zuora/sdk/samples/sample_usage.csv").getFile();
  static final int SAMPLE_WAIT = 15000;

  @Test
  public void test_usage() {
    // Create a Z_Client object
    ZClient zClient = new ZClient(getConfiguration());

    // Create a z_client object and pass it to APIRepo
    UsageManager usageManager = new UsageManager(zClient);

    // Connect to the End Point using public voidault tenant's credentials
    // and practice APIs
    if (new ConnectionManager().isConnected(zClient)) {
      usageManager.create(SAMPLE_USAGE_FILE);

      ZAPIResp resp = usageManager.get(SAMPLE_ACCOUNT_KEY);

      // follow nextPage if present
      while (resp != null) {
        String nextPageLink = (String) resp.get("nextPage");
        if (nextPageLink == null) {
          resp = null;
        } else {
          resp = usageManager.get(SAMPLE_ACCOUNT_KEY, nextPageLink);
        }
      }
    }
  }
}
