package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class JournalEntryManager {
	private ZClient zClient;
	 
    public JournalEntryManager(ZClient zClient){
       this.zClient = zClient;
    }
	public void updateBasicInfo(String jeNumber){
	      try {
	    	  jeNumber = URLEncoder.encode(jeNumber, "UTF-8");
	      } catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	        return;
	      }
	      ZAPIArgs args = new ZAPIArgs();
	      args.set("uri", ResourceEndpoints.PUT_JOURNAL_ENTRY_BASIC_INFORMATION.replace("{je-number}",jeNumber));


	      args.set("reqBody", new ZAPIArgs());
	      args.getArg("reqBody").set("notes", "Soho Networks");
	      args.getArg("reqBody").set("transferredToAccounting", "Yes");
	      args.getArg("reqBody").set("je_picklist1__c", "123");
	      args.getArg("reqBody").set("je_text1__c", "Phase 1");
	     
	      args.getArg("reqBody").setArray("journalEntryItems");
         args.getArg("reqBody").set("journalEntryItems[0]",  new ZAPIArgs());
         args.getArg("reqBody").getArg("journalEntryItems[0]").set("accountingCodeType", "Deferred Revenue");
         args.getArg("reqBody").getArg("journalEntryItems[0]").set("accountingCodeName", "Accounts Receivable");
         args.getArg("reqBody").getArg("journalEntryItems[0]").set("type", "Credit");
         args.getArg("reqBody").getArg("journalEntryItems[0]").set("jei_picklist1__c", "123");
         args.getArg("reqBody").getArg("journalEntryItems[0]").set("jei_text1__c", "item 1");
	      
	      System.out.println("========== UPDATE BASIC INFO ============");

	      try {
	        ZAPIResp response = zClient.put(args);
	        System.out.println(response.toJSONString());
	      } catch (IllegalArgumentException e) {
	        System.out.println(e.getMessage());
	      } catch (RuntimeException e) {
	        System.out.println(e.getMessage());
	      }
	   }
	
	public void createJournalEntry(){
		 ZAPIArgs args = new ZAPIArgs();
	     args.set("uri", ResourceEndpoints.POST_JOURNAL_ENTRY);


	      args.set("reqBody", new ZAPIArgs());
	      args.getArg("reqBody").set("journalEntryDate", "2014-11-30");
	      args.getArg("reqBody").set("accountingPeriodName", "Nov-2014");
	      args.getArg("reqBody").set("notes", "my account");
	      args.getArg("reqBody").set("transferredToAccounting", "Ignore"); 
	      args.getArg("reqBody").set("currency", "USD");
	      
	      
	      args.getArg("reqBody").setArray("segments");
	      args.getArg("reqBody").set("segments[0]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("segments[0]").set("segmentName", "billToCountry");
	      args.getArg("reqBody").getArg("segments[0]").set("segmentValue", "United States");
	      args.getArg("reqBody").set("segments[1]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("segments[1]").set("segmentName", "billToState");
	      args.getArg("reqBody").getArg("segments[1]").set("segmentValue", "California");
	      
	      args.getArg("reqBody").setArray("journalEntryItems");
	      args.getArg("reqBody").set("journalEntryItems[0]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("journalEntryItems[0]").set("accountingCodeType", "Deferred Revenue");
	      args.getArg("reqBody").getArg("journalEntryItems[0]").set("accountingCodeName", "Accounts Receivable");
	      args.getArg("reqBody").getArg("journalEntryItems[0]").set("type", "Credit");
	      args.getArg("reqBody").getArg("journalEntryItems[0]").set("amount", " 400.9");
	      args.getArg("reqBody").getArg("journalEntryItems[0]").set("homeCurrencyAmount", "801.8");
	      
	      args.getArg("reqBody").set("journalEntryItems[1]",  new ZAPIArgs());
	      args.getArg("reqBody").getArg("journalEntryItems[1]").set("accountingCodeName", "Subscription Revenue");
	      args.getArg("reqBody").getArg("journalEntryItems[1]").set("type", "Debit");
	      args.getArg("reqBody").getArg("journalEntryItems[1]").set("amount", " 400.9");
	      args.getArg("reqBody").getArg("journalEntryItems[1]").set("homeCurrencyAmount", "801.8");
  
	      System.out.println("========== CREATE JOURNAL Entry ============");

	      try {
	        ZAPIResp response = zClient.post(args);
	        System.out.println(response.toJSONString());
	      } catch (IllegalArgumentException e) {
	        System.out.println(e.getMessage());
	      } catch (RuntimeException e) {
	        System.out.println(e.getMessage());
	      }
	}
	
	 // Get Account Summary
	  public void getJournalEntriesByJournalRunNumber(String jrNumber) {
	    try {
	    	jrNumber = URLEncoder.encode(jrNumber, "UTF-8");
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	      return;
	    }
	    
	    ZAPIArgs args = new ZAPIArgs();
	    args.set("uri", ResourceEndpoints.GET_JOURNAL_ENTRIES_BY_JOURNAL_RUN_NUMBER.replace("{jr-number}",jrNumber ));

	    System.out.println( "========== GET JOURNAL ENTRIES BY JOURNAL RUN NUMBER ============");

	    try {
	      ZAPIResp response = zClient.get(args);
	      System.out.print(response.toJSONString());
	    } catch (IllegalArgumentException e) {
	      System.out.println(e.getMessage());
	    } catch (RuntimeException e) {
	      System.out.println(e.getMessage());
	    }
	  }
	  
	  // Get Account Summary
     public void getJournalEntryByJournalEntryNumber(String jeNumber) {
       try {
         jeNumber = URLEncoder.encode(jeNumber, "UTF-8");
       } catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         return;
       }
       
       ZAPIArgs args = new ZAPIArgs();
       args.set("uri", ResourceEndpoints.GET_JOURNAL_ENTRY_BY_JOURNAL_ENTRY_NUMBER.replace("{je-number}",jeNumber ));

       System.out.println( "========== GET JOURNAL ENTRY BY JOURNAL ENTRY NUMBER ============");

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
