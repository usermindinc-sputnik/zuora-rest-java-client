package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class RevenueEventManager {
   private ZClient zClient;

   public RevenueEventManager(ZClient zClient){
      this.zClient = zClient;
   }

   // GET REVENUE EVENT DETAIL
   public void getDetailByNumber(String rsNumber) {
     try {
        rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }

     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_EVENTS_DETAIL.replace("{event-number}", rsNumber));

     System.out.println( "========== GET REVENUE EVENT DETAIL ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }

   // GET REVENUE EVENTs
   public void getEventsByRevenueSchedule(String rsNumber) {
     try {
    	 rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }

     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_EVENTS_BY_REVENUE_SCHEDULE.replace("{rs-number}", rsNumber));

     System.out.println( "========== GET REVENUE EVENTs ============");

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
