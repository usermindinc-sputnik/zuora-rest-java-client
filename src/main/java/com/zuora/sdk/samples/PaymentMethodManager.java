/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to use the PaymentMethods resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class PaymentMethodManager {
  static final String SAMPLE_ACCOUNT_KEY = "A00001069";
  private ZClient zClient;
  private String accountKey;

  PaymentMethodManager(ZClient zClient) {
    this.zClient = zClient;
  }

  // Get Credit Cards
  public ZAPIResp getCreditCards(String accountKey, String nextPage) {
	try {
	  this.accountKey = URLEncoder.encode(accountKey, "UTF-8");
	} catch (Exception e) {
	  System.out.println(e.getMessage());
	  e.printStackTrace();
	  return null;
	}
	  
    ZAPIArgs args = new ZAPIArgs();
    if (nextPage != null) {
      args.set("uri", nextPage);
    } else {
      args.set("uri", ResourceEndpoints.GET_CREDIT_CARDS.replace("{account-key}", this.accountKey));
      args.set("queryString", new ZAPIArgs());
      args.getArg("queryString").set("pageSize", 2);
    }

    System.out.println("========== GET CREDIT CARDS ============");

    ZAPIResp response = null;
    try {
      ZAPIResp resp = zClient.get(args);
      System.out.println(resp.toJSONString());
      if ((Integer)resp.get("httpStatusCode") == 200 && (Boolean)resp.get("success")) {
        response = resp;
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

    return response;
  }

  public ZAPIResp getCreditCards(String accountKey) {
    return getCreditCards(accountKey, null);
  }

  // create a credit card then delete it
  public String createCreditCard(String accountKey) {
    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.POST_CREDIT_CARD);

    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("accountKey", accountKey);
    args.getArg("reqBody").set("creditCardType", "Visa");
    args.getArg("reqBody").set("creditCardNumber", "4856200223544175");
    args.getArg("reqBody").set("expirationMonth", "10");
    args.getArg("reqBody").set("expirationYear", "2015");
    args.getArg("reqBody").set("securityCode", "111");
    args.getArg("reqBody").set("defaultPaymentMethod", false);
    args.getArg("reqBody").set("cardHolderInfo", new ZAPIArgs());
    args.getArg("reqBody").getArg("cardHolderInfo").set("cardHolderName", "joe Chan");
    args.getArg("reqBody").getArg("cardHolderInfo").set("addressLine1", "101 Phantom St");
    args.getArg("reqBody").getArg("cardHolderInfo").set("addressLine2", "");
    args.getArg("reqBody").getArg("cardHolderInfo").set("city", "Orinda");
    args.getArg("reqBody").getArg("cardHolderInfo").set("state", "California");
    args.getArg("reqBody").getArg("cardHolderInfo").set("zipCode", "94534");
    args.getArg("reqBody").getArg("cardHolderInfo").set("country", "USA");
    args.getArg("reqBody").getArg("cardHolderInfo").set("phone", "912-2345432");
    args.getArg("reqBody").getArg("cardHolderInfo").set("email", "helloWorld@blackhole.com");
    
    System.out.println("========== CREATE AN CREDIT CARD ============");

    try {
      ZAPIResp response = zClient.post(args);
      System.out.println(response.toJSONString());
      if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
        return (String)response.get("paymentMethodId");
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  // Update Credit Card
  public void updateCreditCard(String paymentMethodId) {
    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.PUT_CREDIT_CARD.replace("{payment-method-id}", paymentMethodId));

    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("expirationMonth", 8);
    args.getArg("reqBody").set("expirationYear", 2015);
    args.getArg("reqBody").set("securityCode", "111");
    args.getArg("reqBody").set("cardHolderName", "Leo");

    System.out.println("========== UPDATE A CREDIT CARD ============");

    try {
      ZAPIResp response = zClient.put(args);
      System.out.println(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }

  // Delete Payment Method
  public void deleteCreditCard(String paymentMethodId) {
    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.DELETE_PAYMENT_METHOD.replace("{payment-method-id}", paymentMethodId));

    System.out.println("========== DELETE A PAYMENT METHOD ============");

    try {
      ZAPIResp response = zClient.delete(args);
      System.out.println(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }

}
