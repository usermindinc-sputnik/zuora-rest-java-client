/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to use the Transactions resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.net.URLEncoder;

public class TransactionManager {
  static final String SAMPLE_ACCOUNT_KEY = "A00001069";
  private ZClient zClient;
  private String accountKey;

  TransactionManager(ZClient zClient) {
    this.zClient = zClient;
  }

  // Get Invoices
  public ZAPIResp getInvoices(String accountKey, String nextPage) {
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
      args.set("uri", ResourceEndpoints.GET_INVOICES.replace("{account-key}", this.accountKey));
      args.set("queryString", new ZAPIArgs());
      args.getArg("queryString").set("pageSize", 10);
    }

    System.out.println("========== GET INVOICES ============");

    ZAPIResp response = null;
    try {
      response = zClient.get(args);
      System.out.println(response.toJSONString());
      if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
        return response;
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public ZAPIResp getInvoices(String accountKey) {
    return getInvoices(accountKey, null);
  }

  // Get Payments
  public ZAPIResp getPayments(String accountKey, String nextPage) {
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
      args.set("uri", ResourceEndpoints.GET_PAYMENTS.replace("{account-key}", this.accountKey));
      args.set("queryString", new ZAPIArgs());
      args.getArg("queryString").set("pageSize", 10);
    }

    System.out.println("========== GET PAYMENTS ============");

    ZAPIResp response = null;
    try {
      response = zClient.get(args);
      System.out.println(response.toJSONString());
      if ((Integer)response.get("httpStatusCode") == 200 && (Boolean)response.get("success")) {
        return response;
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }  catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public ZAPIResp getPayments(String accountKey) {
    return getPayments(accountKey, null);
  }

}
