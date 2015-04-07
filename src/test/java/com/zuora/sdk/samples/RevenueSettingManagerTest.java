package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class RevenueSettingManagerTest extends BaseZuoraApiTest {

   @Test
   public void test_get_revenue_events(){
      // Create a z_client
      ZClient zClient = new ZClient(getConfiguration());

      // create an revenue setting resource manager
      RevenueSettingManager settingManager = new RevenueSettingManager(zClient);

      if (new ConnectionManager().isConnected(zClient)) {
    	  settingManager.getRevenueAutomationDate();
      }
   }
}
