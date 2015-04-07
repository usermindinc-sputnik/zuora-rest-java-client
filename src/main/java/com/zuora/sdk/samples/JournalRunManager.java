package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class JournalRunManager {
	private ZClient zClient;
	 
    public JournalRunManager(ZClient zClient){
       this.zClient = zClient;
    }
    
	public void createJournalRun(){
		 ZAPIArgs args = new ZAPIArgs();
	     args.set("uri", ResourceEndpoints.POST_JOURNAL_RUN);


	      args.set("reqBody", new ZAPIArgs());
	      args.getArg("reqBody").set("journalEntryDate", "2014-11-30");
	      args.getArg("reqBody").set("accountingPeriodName", "Nov-2014");
	      args.getArg("reqBody").set("targetStartDate", "2014-11-01");
	      args.getArg("reqBody").set("targetEndDate", "2014-11-30");
	      
	      
	      args.getArg("reqBody").setArray("transactionTypes");
	      args.getArg("reqBody").set("transactionTypes[0]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("transactionTypes[0]").set("type", "Invoice Item");
	      args.getArg("reqBody").set("transactionTypes[1]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("transactionTypes[1]").set("type", "Invoice Item Adjustment (Invoice)");
	      args.getArg("reqBody").set("transactionTypes[2]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("transactionTypes[2]").set("type", "Revenue Event Item");
	      
  
	      System.out.println("========== CREATE JOURNAL RUN ============");

	      try {
	        ZAPIResp response = zClient.post(args);
	        System.out.println(response.toJSONString());
	      } catch (IllegalArgumentException e) {
	        System.out.println(e.getMessage());
	      } catch (RuntimeException e) {
	        System.out.println(e.getMessage());
	      }
	      
	      
	}
	
	public void getJournalRun(String jrNumber){
		  try {
			  jrNumber = URLEncoder.encode(jrNumber, "UTF-8");
		     } catch (Exception e) {
		       System.out.println(e.getMessage());
		       e.printStackTrace();
		       return;
		     }
		     
		     ZAPIArgs args = new ZAPIArgs();
		     args.set("uri", ResourceEndpoints.GET_JOURNAL_RUN.replace("{jr-number}", jrNumber));

		     System.out.println( "========== GET JOURNAL RUN INFORMATION ============");

		     try {
		       ZAPIResp response = zClient.get(args);
		       System.out.println(response.toJSONString());
		     } catch (IllegalArgumentException e) {
		       System.out.println(e.getMessage());
		     } catch (RuntimeException e) {
		       System.out.println(e.getMessage());
		     }
	}
}
