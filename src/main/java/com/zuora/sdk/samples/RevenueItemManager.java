package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class RevenueItemManager {
   private ZClient zClient;
   
   public RevenueItemManager(ZClient zClient){
      this.zClient = zClient;
   }
   
   // GET REVENUE ITEM by Charge Revenue Schedule
   public void getItemsByChargeRevenueSummary(String chargeRSNumber) {
     try {
        chargeRSNumber = URLEncoder.encode(chargeRSNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_ITEMS_BY_CHARGE_REVENUE_SUMMARY.replace("{crs-number}", chargeRSNumber));

     System.out.println( "========== GET REVENUE ITEMs by CHARGE REVENUE SCHEDULE ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }

   // GET REVENUE ITEM by Revenue Schedule
   public void getItemsByRevenueSchedule(String rsNumber) {
     try {
        rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_ITEMS_BY_REVENUE_SCHEDULE.replace("{rs-number}", rsNumber));

     System.out.println( "========== GET REVENUE ITEMs by TRANSACTION REVENUE SCHEDULE ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   } 
   
   // GET REVENUE ITEM by Revenue Event
   public void getItemsByRevenueEvent(String eventNumber) {
     try {
        eventNumber = URLEncoder.encode(eventNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_ITEMS_BY_REVENUE_EVENT.replace("{event-number}", eventNumber));

     System.out.println( "========== GET REVENUE ITEMs by REVENUE EVENT ============");

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
