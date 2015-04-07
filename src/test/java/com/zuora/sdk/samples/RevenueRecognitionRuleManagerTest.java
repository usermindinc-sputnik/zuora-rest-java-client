package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class RevenueRecognitionRuleManagerTest extends BaseZuoraApiTest {
   static final String SAMPLE_SUBSCRIPTION_CHARGE_KEY = "402892bf40709ec701407bd4ad040139";

   @Test
   public void test_get_revenue_events(){
      // Create a z_client
      ZClient zClient = new ZClient(getConfiguration());

      // create an revenue event resource manager
      RevenueRecognitionRuleManager ruleManager = new RevenueRecognitionRuleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
    	  ruleManager.getRuleNameBySubscriptionCharge(SAMPLE_SUBSCRIPTION_CHARGE_KEY);
      }
   }
}
