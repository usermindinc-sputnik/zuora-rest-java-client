package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class SubscriptionManagerTest extends BaseZuoraApiTest {
   static final String SAMPLE_ACCOUNT_KEY = "A00001069";

   @Test
   public void test_subscription(){
   // create a Z_Client
      ZClient zClient = new ZClient(getConfiguration());

      // Create a z_client object and pass it to APIRepo
      SubscriptionManager subscriptionManager = new SubscriptionManager(zClient);

      // Connect to the endPoint using default tenant's credentials
      // and practice APIs
      ZAPIResp resp = null;
      if (new ConnectionManager().isConnected(zClient)) {
        resp = subscriptionManager.getAll(SAMPLE_ACCOUNT_KEY);

        // follow nextPage if present
        while (resp != null) {
          String nextPageLink = (String)resp.get("nextPage");
          if (nextPageLink == null) {
            resp = null;
          } else {
            resp = subscriptionManager.getAll(SAMPLE_ACCOUNT_KEY, nextPageLink);
          }
        }

        subscriptionManager.createPreview();

        String subscriptionNumber = subscriptionManager.create();
        if (subscriptionNumber != null) {
          resp = subscriptionManager.get(subscriptionNumber);
          if (resp != null) {
            subscriptionManager.update((String)resp.get("subscriptionNumber"), (String)resp.getResp("ratePlans[0]").get("id"), (String)resp.getResp("ratePlans[0]").getResp("ratePlanCharges[0]").get("id"));
            subscriptionManager.renew((String)resp.get("subscriptionNumber"));
            subscriptionManager.cancel((String)resp.get("subscriptionNumber"));
          }
        }
      }
   }
}
