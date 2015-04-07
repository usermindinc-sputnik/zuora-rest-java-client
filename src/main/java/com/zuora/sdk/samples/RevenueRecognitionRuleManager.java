package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class RevenueRecognitionRuleManager {
   private ZClient zClient;

   public RevenueRecognitionRuleManager(ZClient zClient){
      this.zClient = zClient;
   }

   // GET RULE NAME BY SUBCRIPTION CHARGE
   public void getRuleNameBySubscriptionCharge(String chargeId) {
	   System.out.println("charge_id"+chargeId);
     try {
        chargeId = URLEncoder.encode(chargeId, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }

     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_RECOGNITION_RULE_BY_SUBSCRIPTION_CHARGE.replace("{charge-id}", chargeId));

     System.out.println( "========== GET REVENUE RECOGNITION RULE BY SUBSCRIPTION CHARGE ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }
}
