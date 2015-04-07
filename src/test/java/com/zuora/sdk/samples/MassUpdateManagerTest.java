package com.zuora.sdk.samples;

import org.junit.Ignore;

import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class MassUpdateManagerTest extends BaseZuoraApiTest {
   static final String SAMPLE_MassUpdate_FILE = MassUpdateManager.class.getClassLoader().getResource("com/zuora/sdk/samples/CreateRevenueSchedulesTemplate.csv").getFile();
   static final int SAMPLE_WAIT = 15000;

   @Test
   @Ignore // this test doesn't work even in original project
   public void test_massUpdate(){
      // Create a Z_Client object
      ZClient zClient = new ZClient(getConfiguration());

      // Create a z_client object and pass it to APIRepo
      MassUpdateManager massUpdateManager = new MassUpdateManager(zClient);

      // Connect to the End Point using public voidault tenant's credentials
      // and practice APIs
      if (new ConnectionManager().isConnected(zClient)) {
         String actionKey = massUpdateManager.create(SAMPLE_MassUpdate_FILE, "{actionType:CreateRevenueSchedule}");

        ZAPIResp resp = massUpdateManager.get(actionKey);
        resp.toJSONString();
      }
   }
}
