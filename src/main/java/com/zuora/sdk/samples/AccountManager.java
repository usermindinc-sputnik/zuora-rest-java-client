/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to use the Accounts resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class AccountManager {
  static final String SAMPLE_ACCOUNT_KEY = "A00001069";
  private ZClient zClient;
  private String accountKey;

  AccountManager(ZClient zClient) {
    this.zClient = zClient;
  }

  // Get Account Summary
  public void getSummary(String accountKey) {
    try {
  	  this.accountKey = URLEncoder.encode(accountKey, "UTF-8");
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return;
    }

    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.GET_ACCOUNT_SUMMARY.replace("{account-key}", this.accountKey));

    System.out.println( "========== GET ACCOUNT SUMMARY ============");

    try {
      ZAPIResp response = zClient.get(args);
      System.out.print(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }

  // Get Account Details
  public void getDetails(String accountKey) {
	try {
	  this.accountKey = URLEncoder.encode(accountKey, "UTF-8");
	} catch (Exception e) {
	  System.out.println(e.getMessage());
	  e.printStackTrace();
	  return;
	}

	ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.GET_ACCOUNT_DETAIL.replace("{account-key}", this.accountKey));

    System.out.println( "========== GET ACCOUNT DETAILS ============");

    try {
      ZAPIResp response = zClient.get(args);
      System.out.print(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }

  // create an account
  public String create() {
    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.POST_ACCOUNT);

    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("name", "Soho");
    args.getArg("reqBody").set("currency", "USD");
    args.getArg("reqBody").set("notes", "Soho Networks");
    args.getArg("reqBody").set("billCycleDay", "15");
    args.getArg("reqBody").set("paymentTerm", "Net 30");
    args.getArg("reqBody").set("autoPay", true);
    args.getArg("reqBody").set("ac_Winnie__c", "ac_Winnie");
    args.getArg("reqBody").set("ac_Willie__c", "ac_boy");
    args.getArg("reqBody").set("billToContact", new ZAPIArgs());
    args.getArg("reqBody").getArg("billToContact").set("address1", "address1");
    args.getArg("reqBody").getArg("billToContact").set("address2", "address2");
    args.getArg("reqBody").getArg("billToContact").set("city", "San Francisco");
    args.getArg("reqBody").getArg("billToContact").set("state", "California");
    args.getArg("reqBody").getArg("billToContact").set("country", "USA");
    args.getArg("reqBody").getArg("billToContact").set("firstName", "John");
    args.getArg("reqBody").getArg("billToContact").set("lastName", "Doe");
    args.getArg("reqBody").getArg("billToContact").set("mobilePhone", "14156789012");
    args.getArg("reqBody").getArg("billToContact").set("workEmail", "john.doe@zoura.com");

    args.getArg("reqBody").set("soldToContact", new ZAPIArgs());
    args.getArg("reqBody").getArg("soldToContact").set("address1", "address1");
    args.getArg("reqBody").getArg("soldToContact").set("address2", "address2");
    args.getArg("reqBody").getArg("soldToContact").set("city", "San Francisco");
    args.getArg("reqBody").getArg("soldToContact").set("country", "USA");
    args.getArg("reqBody").getArg("soldToContact").set("firstName", "Jane");
    args.getArg("reqBody").getArg("soldToContact").set("lastName", "Doe");
    args.getArg("reqBody").getArg("soldToContact").set("mobilePhone", "14156789012");
    args.getArg("reqBody").getArg("soldToContact").set("state", "California");
    args.getArg("reqBody").getArg("soldToContact").set("workEmail", "jane.doe@zoura.com");

    args.getArg("reqBody").set("creditCard", new ZAPIArgs());
    args.getArg("reqBody").getArg("creditCard").set("cardType", "Visa");
    args.getArg("reqBody").getArg("creditCard").set("cardNumber", "4856200223544175");
    args.getArg("reqBody").getArg("creditCard").set("expirationMonth", 2);
    args.getArg("reqBody").getArg("creditCard").set("expirationYear", 2014);
    args.getArg("reqBody").getArg("creditCard").set("securityCode", "111");

    System.out.println( "========== CREATE AN ACCOUNT ============");

    try {
      ZAPIResp response = zClient.post(args);
      System.out.println(response.toJSONString());
      if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
        return (String)response.get("accountNumber");
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  // Update an Account
  public void update(String accountKey) {
	try {
	  this.accountKey = URLEncoder.encode(accountKey, "UTF-8");
	} catch (Exception e) {
	  System.out.println(e.getMessage());
	  e.printStackTrace();
	  return;
	}

    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.PUT_ACCOUNT.replace("{account-key}", this.accountKey));

    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("billToContact", new ZAPIArgs());
    args.getArg("reqBody").getArg("billToContact").set("homePhone", "9259259259");
    args.getArg("reqBody").getArg("billToContact").set("zipCode", "94549");

    System.out.println( "========== UPDATE AN ACCOUNT ============");

    try {
      ZAPIResp response = zClient.put(args);
      System.out.println(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }

  // Create an Account with subscription in one go
  public void createWithSubscription() {
    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.POST_ACCOUNT);

    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("name", "Soho");
    args.getArg("reqBody").set("currency", "USD");
    args.getArg("reqBody").set("notes", "Soho Networks");
    args.getArg("reqBody").set("billCycleDay", "15");
    args.getArg("reqBody").set("paymentTerm", "Net 30");
    args.getArg("reqBody").set("autoPay", false);
    args.getArg("reqBody").set("ac_Winnie__c", "cf v");
    args.getArg("reqBody").set("ac_Willie__c", "ac_boy");
    args.getArg("reqBody").set("billToContact", new ZAPIArgs());
    args.getArg("reqBody").getArg("billToContact").set("address1", "address1");
    args.getArg("reqBody").getArg("billToContact").set("address2", "address2");
    args.getArg("reqBody").getArg("billToContact").set("city", "San Francisco");
    args.getArg("reqBody").getArg("billToContact").set("state", "California");
    args.getArg("reqBody").getArg("billToContact").set("country", "USA");
    args.getArg("reqBody").getArg("billToContact").set("firstName", "Jane");
    args.getArg("reqBody").getArg("billToContact").set("lastName", "Doe");
    args.getArg("reqBody").getArg("billToContact").set("mobilePhone", "14156789012");
    args.getArg("reqBody").getArg("billToContact").set("workEmail", "jane.doe@zoura.com");

    args.getArg("reqBody").set("soldToContact", new ZAPIArgs());
    args.getArg("reqBody").getArg("soldToContact").set("address1", "address1");
    args.getArg("reqBody").getArg("soldToContact").set("address2", "address2");
    args.getArg("reqBody").getArg("soldToContact").set("city", "San Francisco");
    args.getArg("reqBody").getArg("soldToContact").set("country", "USA");
    args.getArg("reqBody").getArg("soldToContact").set("firstName", "John");
    args.getArg("reqBody").getArg("soldToContact").set("lastName", "Doe");
    args.getArg("reqBody").getArg("soldToContact").set("mobilePhone", "14156789012");
    args.getArg("reqBody").getArg("soldToContact").set("state", "California");
    args.getArg("reqBody").getArg("soldToContact").set("workEmail", "john.doe@zoura.com");

    args.getArg("reqBody").set("creditCard", new ZAPIArgs());
    args.getArg("reqBody").getArg("creditCard").set("cardType", "Visa");
    args.getArg("reqBody").getArg("creditCard").set("cardNumber", "4856200223544175");
    args.getArg("reqBody").getArg("creditCard").set("expirationMonth", 2);
    args.getArg("reqBody").getArg("creditCard").set("expirationYear", 2014);
    args.getArg("reqBody").getArg("creditCard").set("securityCode", "111");

    args.getArg("reqBody").set("subscription", new ZAPIArgs());
    args.getArg("reqBody").getArg("subscription").set("contractEffectiveDate", "2013-02-1");
    args.getArg("reqBody").getArg("subscription").set("termType", "TERMED");
    args.getArg("reqBody").getArg("subscription").set("initialTerm", "12");
    args.getArg("reqBody").getArg("subscription").set("autoRenew", true);
    args.getArg("reqBody").getArg("subscription").set("renewalTerm", "3");
    args.getArg("reqBody").getArg("subscription").set("notes", "Good deal");
    args.getArg("reqBody").set("invoiceTargetDate", "2013-12-31");
    args.getArg("reqBody").set("invoiceCollect", true);
    args.getArg("reqBody").getArg("subscription").setArray("subscribeToRatePlans");
    args.getArg("reqBody").getArg("subscription").set("subscribeToRatePlans[0]", new ZAPIArgs());
    args.getArg("reqBody").getArg("subscription").getArg("subscribeToRatePlans[0]").set("productRatePlanId", "8a8582933b24b3cd013b250572f50faf");
    args.getArg("reqBody").getArg("subscription").getArg("subscribeToRatePlans[0]").setArray("chargeOverrides");
    args.getArg("reqBody").getArg("subscription").getArg("subscribeToRatePlans[0]").set("chargeOverrides[0]", new ZAPIArgs());
    args.getArg("reqBody").getArg("subscription").getArg("subscribeToRatePlans[0]").getArg("chargeOverrides[0]").set("productRatePlanChargeId", "8a8582933b24b3cd013b25067e660fb1");
    args.getArg("reqBody").getArg("subscription").getArg("subscribeToRatePlans[0]").getArg("chargeOverrides[0]").set("quantity", 10);

    System.out.println( "========== CREATE AN ACCOUNT WITH SUBSCRIPTION ============");

    try {
      ZAPIResp response = zClient.post(args);
      System.out.println(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }
}
