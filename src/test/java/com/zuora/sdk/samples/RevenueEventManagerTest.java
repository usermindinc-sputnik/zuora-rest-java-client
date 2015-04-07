package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class RevenueEventManagerTest extends BaseZuoraApiTest{
   static final String SAMPLE_REVENUE_SCHEDULE_KEY = "RS-00000001";
   static final String SAMPLE_REVENUE_EVENT_KEY = "RE-00000001";

   @Test
   public void test_get_revenue_events(){
      // Create a z_client
      ZClient zClient = new ZClient(getConfiguration());

      // create an revenue event resource manager
      RevenueEventManager reManager = new RevenueEventManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         reManager.getDetailByNumber(SAMPLE_REVENUE_EVENT_KEY);
         reManager.getEventsByRevenueSchedule(SAMPLE_REVENUE_SCHEDULE_KEY);
      }
   }
}
