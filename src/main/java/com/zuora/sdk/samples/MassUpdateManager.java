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

public class MassUpdateManager {
  static final String BULK_KEY="bulkKey";
  static final int SAMPLE_WAIT = 10000;
  static final String[] EXTENSION = {".csv",".zip"};
  private ZClient zClient;
  private String bulkKey;
  
  MassUpdateManager(ZClient zClient) {
    this.zClient = zClient;
  }

  public ZAPIResp get(String bulkKey) {
	try {
	  this.bulkKey = URLEncoder.encode(bulkKey, "UTF-8");
	} catch (Exception e) {
	  System.out.println(e.getMessage());
	  e.printStackTrace();
	  return null;
	}
    ZAPIArgs args = new ZAPIArgs();

    args.set("uri", ResourceEndpoints.PATH_MASS_UPDATE_KEY.replace("{bulk-key}", this.bulkKey));
    args.set("queryString", new ZAPIArgs());

    System.out.println("========== GET MASS UPDATE RESULT ============");

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

  public String create(String filename, String params) {
    System.out.println( "========== UPLOAD MASS CreateRevenueSchedle FILE ============");

    // Reject if file not found
    if (!(new File(filename).isFile())) {
      System.out.println(filename + " File not found.");
      return null;
    }

    // Reject if wrong file type
    String extension = filename.substring(filename.lastIndexOf('.'));
    for (int i = 0; i < EXTENSION.length; i++) {
      if (extension.toLowerCase().equals(EXTENSION[i])) {break;}
      if (i == 1) {
        System.out.println( "Failed to upload " + filename +" File extension not supported. Must be .csv or .zip ");
        return null;
      }
    }

    ZAPIArgs args = new ZAPIArgs();
    args.set("uri", ResourceEndpoints.PATH_MASS_UPDATE);
    args.set("reqBody", new ZAPIArgs());
    args.getArg("reqBody").set("filename", filename);
    args.set("params", params);


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
      (Boolean)response.get("success") && (String)response.get(BULK_KEY) != null) {
     
      // hang in there a bit and see if import worked
      try {
        Thread.sleep(SAMPLE_WAIT);
      } catch (Exception e) {};
      
      this.bulkKey = (String)response.get(BULK_KEY);
      args = new ZAPIArgs();
      args.set(BULK_KEY, (String)response.get(BULK_KEY));

      System.out.println("========== GET IMPORT ID ============");

      try {
       response = zClient.get(args);
       System.out.println(response.toJSONString());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
      }
      return bulkKey;
    }
     return null;
  }
}
