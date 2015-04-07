/**
 * Copyright (c) 2013 Zuora Inc.
 *
 * Sample code to demonstrate how to use the Usage resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIArgs;
import com.zuora.sdk.lib.ZAPIResp;
import com.zuora.sdk.lib.ZClient;

import java.io.File;
import java.net.URLEncoder;

public class UsageManager {
  static final String SAMPLE_ACCOUNT_KEY = "A00001069";
  static final String SAMPLE_USAGE_FILE = "/home/user/NetBeansProjects/zuora-rest-java/src/com/zuora/sdk/samples/sample_usage.csv";
  static final int SAMPLE_WAIT = 15000;
  static final String[] EXTENSION = {".xls", ".csv",".zip"};
  private ZClient zClient;
  private String accountKey;

  UsageManager(ZClient zClient) {
    this.zClient = zClient;
  }
  // Get Usage
  public ZAPIResp get(String accountKey, String nextPage) {
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
      args.set("uri", ResourceEndpoints.GET_USAGE.replace("{account-key}", this.accountKey));
      args.set("queryString", new ZAPIArgs());
      args.getArg("queryString").set("pageSize", 10);
    }

    System.out.println("========== GET USAGE ============");

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

  public ZAPIResp get(String accountKey) {
    return get(accountKey, null);
  }

  // Upload a zipped usage file
  public void create(String fullyQualifiedFilename) {
    System.out.println( "========== UPLOAD A USAGE FILE ============");

    // Reject if file not found
    if (!(new File(fullyQualifiedFilename).isFile())) {
      System.out.println("Failed to upload usage file " + fullyQualifiedFilename + ". File not found.");
      return;
    }

    // Reject if wrong file type
    String extension = fullyQualifiedFilename.substring(fullyQualifiedFilename.lastIndexOf('.'));
    for (int i = 0; i < EXTENSION.length; i++) {
      if (extension.toLowerCase().equals(EXTENSION[i])) {break;}
      if (i ==2) {
        System.out.println( "Failed to upload usage file " + fullyQualifiedFilename);
        System.out.println( "File extension not supported. Must be .csv, .xls, or .zip");
        return;
      }
    }

    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.POST_USAGE);
    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("filename", fullyQualifiedFilename);

    ZAPIResp response = null;
    try {
      response = zClient.post(args);
      System.out.println(response.toJSONString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

    if (response != null && (Integer)response.get("httpStatusCode") == 200 &&
      (Boolean)response.get("success") && (String)response.get("checkImportStatus") != null) {
     
      // hang in there a bit and see if import worked
      try {
        Thread.sleep(SAMPLE_WAIT);
      } catch (Exception e) {};

      args = new ZAPIArgs();
      args.set("uri", (String)response.get("checkImportStatus"));

      System.out.println("========== GET IMPORT STATUS ============");

      try {
       response = zClient.get(args);
       System.out.println(response.toJSONString());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
