package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZAPIResp;

import java.util.Iterator;

public class ZAPIRespIterator {

  public static void iterateZAPIResp(ZAPIResp resp) {
    // Iterate the entire response set based on ZAPIResp
    // Refer to the API references for the attribute structure

    // get iterator for the products
    Iterator<ZAPIResp> iteProds = resp.iterator("products");

    // for each product
    while (iteProds.hasNext()) {

      // get the collection
      ZAPIResp prod = iteProds.next();

      // display some attributes
      System.out.println(prod.get("sku"));
      System.out.println(prod.get("name"));
      System.out.println(prod.get("description"));
      System.out.println(prod.get("P_Winnie__c"));
      System.out.println(prod.get("P_Willie__c"));

      // get iterator for productRatePlans
      Iterator<ZAPIResp> itePrps = prod.iterator("productRatePlans");

      // for each product rate plan
      while (itePrps.hasNext()) {

        // get the collection
        ZAPIResp prp = itePrps.next();

        // display some attributes
        System.out.println(prp.get("id"));
        System.out.println(prp.get("status"));
        System.out.println(prp.get("name"));
        System.out.println(prp.get("description"));
        System.out.println(prp.get("id"));
        System.out.println(prp.get("effectiveStartDate"));
        System.out.println(prp.get("effectiveEndDate"));

        // display custom fields
        System.out.println(prp.get("PRP_Winnie__c"));
        System.out.println(prp.get("PRP_Willie__c"));

        // get iterator for productRatePlanCharges
        Iterator<ZAPIResp> itePrpChargeSummaries = prp.iterator("productRatePlanCharges");

        // for each product rate plan charge summary
        while (itePrpChargeSummaries.hasNext()) {

          // get the collection
          ZAPIResp chargeSummary = itePrpChargeSummaries.next();

          // display some attributes
          System.out.println(chargeSummary.get("id"));
          System.out.println(chargeSummary.get("name"));
          System.out.println(chargeSummary.get("type"));
          System.out.println(chargeSummary.get("model"));

          // get the iterator for pricingSummary
          Iterator<ZAPIResp> itePricingSummary = chargeSummary.iterator("pricingSummary");

          // display every priceSummary
          while (itePricingSummary.hasNext()) {
            System.out.println(itePricingSummary.next());
          }

          // display more attributes of chargeSummary
          System.out.println(chargeSummary.get("billingDay"));
          System.out.println(chargeSummary.get("billingPeriod"));
          System.out.println(chargeSummary.get("taxable"));
          System.out.println(chargeSummary.get("taxCode"));
          System.out.println(chargeSummary.get("taxMode"));
        }
      }

      // display nextPage link if is non-null
      if (prod.get("nextPage") != null) {
        System.out.println(prod.get("netPage"));
      }
    }
  }

}
