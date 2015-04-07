package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;
import java.util.Map;

public class RevenueScheduleManager {
   private ZClient zClient;
   public final static String SIMPLE_RULE_NAME="";
   
   public RevenueScheduleManager(ZClient zClient){
      this.zClient = zClient;
   }
   
   // GET TRANSACTION REVENUE SCHEDULE DETAIL
   public void getDetailByNumber(String rsNumber) {
     try {
        rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_SCHEDULE_DETAIL.replace("{rs-number}", rsNumber));

     System.out.println( "========== GET TRANSACTION REVENUE SCHEDULE DETAIL ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }
   
   // GET REVENUE SCHEDULEs DETAILs BY CHARGE
   public void getDetailsByCharge(String chargeKey) {
     try {
    	 chargeKey = URLEncoder.encode(chargeKey, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_SCHEDULE_DETAILS_BY_CHARGE.replace("{charge-key}", chargeKey));

     System.out.println( "========== GET REVENUE SCHEDULE DETAILS BY CHARGE ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }
      
   // GET TRANSACTION REVENUE SCHEDULE DETAIL
   public void getDetailByInvoiceItem(String invoiceItemKey) {
     try {
        invoiceItemKey = URLEncoder.encode(invoiceItemKey, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_SCHEDULE_DETAIL_BY_INVOICE_ITEM.replace("{invoice-item-key}", invoiceItemKey));

     System.out.println( "========== GET TRANSACTION REVENUE SCHEDULE DETAIL ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }
   
   // GET TRANSACTION REVENUE SCHEDULE DETAIL
   public void getDetailByInvoiceItemAdjustment(String invoiceItemAdjustmentKey) {
     try {
        invoiceItemAdjustmentKey = URLEncoder.encode(invoiceItemAdjustmentKey, "UTF-8");
     } catch (Exception e) {
       System.out.println(e.getMessage());
       e.printStackTrace();
       return;
     }
     
     ZAPIArgs args = new ZAPIArgs();
     args.set("uri", ResourceEndpoints.GET_REVENUE_SCHEDULE_DETAIL_BY_INVOICE_ITEM_ADJUSTMENT.replace("{invoice-item-adj-key}", invoiceItemAdjustmentKey));

     System.out.println( "========== GET TRANSACTION REVENUE SCHEDULE DETAIL ============");

     try {
       ZAPIResp response = zClient.get(args);
       System.out.print(response.toJSONString());
     } catch (IllegalArgumentException e) {
       System.out.println(e.getMessage());
     } catch (RuntimeException e) {
       System.out.println(e.getMessage());
     }
   }   
   
   public void distributeRevenueRatably(String rsNumber){
      try {
         rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        return;
      }
      ZAPIArgs args = new ZAPIArgs();
      args.set("uri", ResourceEndpoints.PUT_REVENUE_SCHEDULE_DISTRIBUTE_RATABLY.replace("{rs-number}",rsNumber));

      args.set("reqBody", new ZAPIArgs());
      args.getArg("reqBody").set("recognitionStart", "2013-02-1");
      args.getArg("reqBody").set("recognitionEnd", "2013-12-1");
      args.getArg("reqBody").set("eventType", "Revenue Distributed");
      args.getArg("reqBody").set("eventTypeSystemId", "RevenueDistributed__z");
      args.getArg("reqBody").set("notes", "Soho Networks");
      args.getArg("reqBody").set("cf_project__c", "Project A");
      args.getArg("reqBody").set("cf_phase__c", "Phase 1");
      
      System.out.println("========== UPDATE RECOGNITION TERM ============");

      try {
        ZAPIResp response = zClient.put(args);
        System.out.println(response.toJSONString());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
      }
   }
   
   public void updateBasicInfo(String rsNumber){
      try {
         rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        return;
      }
      ZAPIArgs args = new ZAPIArgs();
      args.set("uri", ResourceEndpoints.PUT_REVENUE_SCHEDULE_BASIC_INFORMATION.replace("{rs-number}",rsNumber));


      args.set("reqBody", new ZAPIArgs());
      args.getArg("reqBody").set("notes", "Soho Networks");
      args.getArg("reqBody").set("referenceId", "external RS reference ID");
      args.getArg("reqBody").set("cf_project__c", "Project B");
      args.getArg("reqBody").set("cf_phase__c", "Phase 1");
      
      System.out.println("========== UPDATE BASIC INFOR ============");

      try {
        ZAPIResp response = zClient.put(args);
        System.out.println(response.toJSONString());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
      }
   }
   
   public void distributeRevenueManually(String rsNumber,Map<String,String> distributionItems){
      try {
         rsNumber = URLEncoder.encode(rsNumber, "UTF-8");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        return;
      }
      ZAPIArgs args = new ZAPIArgs();
      args.set("uri", ResourceEndpoints.PUT_REVENUE_SCHEDULE_DISTRIBUTE_MANUALLY.replace("{rs-number}",rsNumber));


      args.set("reqBody", new ZAPIArgs());
      args.getArg("reqBody").set("notes", "Soho Networks");
      args.getArg("reqBody").set("eventType", "Revenue Distributed");
      args.getArg("reqBody").set("eventTypeSystemId", "RevenueDistributed__z");
      args.getArg("reqBody").set("cf_project__c", "Project A");
      args.getArg("reqBody").set("cf_phase__c", "Phase 1");
      args.getArg("reqBody").setArray("revenueDistributions");
      int count=0;
      for(Map.Entry<String, String> item:distributionItems.entrySet()){
         String itemKey = "revenueDistributions["+count+"]";
         args.getArg("reqBody").set(itemKey,new ZAPIArgs());
         args.getArg("reqBody").getArg(itemKey).set("accountingPeriodName", item.getKey());
         args.getArg("reqBody").getArg(itemKey).set("newAmount", item.getValue());
         count++;
      }
      System.out.println(args.toJSONString());
      
      
      System.out.println("========== DISTRIBUTE ============");

      try {
        ZAPIResp response = zClient.put(args);
        System.out.println(response.toJSONString());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
      }
   }  
   
   public String createRSByCharge(String chargeId, Map<String,String> distributionItems) {
      try {
          chargeId = URLEncoder.encode(chargeId, "UTF-8");
       } catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         return null;
       }
	   ZAPIArgs args = new ZAPIArgs();
	   args.set("uri", ResourceEndpoints.POST_REVENUE_SCHEDULE_BY_CHARGE_MANUALLY_DISTRIBUTION.replace("{charge-key}",chargeId));

	   args.set("reqBody", new ZAPIArgs());
	   args.getArg("reqBody").set("referenceId", "reference ID");
	   args.getArg("reqBody").set("amount", "300.00");
	   args.getArg("reqBody").set("notes", "create external revenue schedule by the charge key");
	   args.getArg("reqBody").setArray("revenueDistributions");
	   args.getArg("reqBody").set("revenueScheduleDate", "2013-12-12");
	   args.getArg("reqBody").set("overrideChargeAccountingCodes","true");
	   args.getArg("reqBody").set("recognizedRevenueAccountingCode", "Usage Revenue");
	   args.getArg("reqBody").set("deferredRevenueAccountingCodeType", "Deferred Revenue");
	   args.getArg("reqBody").set("deferredRevenueAccountingCode", "Check");
	   
       int count=0;
       for(Map.Entry<String, String> item:distributionItems.entrySet()){
          String itemKey = "revenueDistributions["+count+"]";
          args.getArg("reqBody").set(itemKey,new ZAPIArgs());
          args.getArg("reqBody").getArg(itemKey).set("accountingPeriodName", item.getKey());
          args.getArg("reqBody").getArg(itemKey).set("newAmount", item.getValue());
          count++;
       }
       args.getArg("reqBody").set("revenueEvent", new ZAPIArgs());
       args.getArg("reqBody").getArg("revenueEvent").set("eventType", "Invoice Posted");
       args.getArg("reqBody").getArg("revenueEvent").set("eventTypeSystemId", "InvoicePosted__z");
       args.getArg("reqBody").getArg("revenueEvent").set("notes", "this is external revenue schedule");
       System.out.println(args.toJSONString());
       
       System.out.println( "========== CREATE AN EXTERNAL REVENUE SCHEDULE ============");

       try {
         ZAPIResp response = zClient.post(args);
         System.out.println(response.toJSONString());
         if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
           return (String)response.get("revenueScheduleNumber");
         }
       } catch (IllegalArgumentException e) {
         System.out.println(e.getMessage());
       } catch (RuntimeException e) {
         System.out.println(e.getMessage());
       }
       return null;	   
   }
}
