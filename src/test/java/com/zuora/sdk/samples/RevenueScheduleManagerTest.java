package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RevenueScheduleManagerTest extends BaseZuoraApiTest{
   static final String SAMPLE_TRANSACTION_REVENUE_SCHEDULE_KEY = "RS-00000001";
   static final String SAMPLE_CHARGE_KEY = "2c9081a03d7e55a6013d7e8dad3a0044";


   static final String SAMPLE_INVOICE_ITEM_KEY = "402892bf40709ec701407bd5c3020145";
   static final String SAMPLE_INVOICE_ITEM_ADJ_KEY = "402892bf40709ec701407be7befc016a";

   static final String SAMPLE_AP1_NAME= "Apr'13";
   static final String SAMPLE_AP2_NAME= "Jan'14";

   @Test
   public void test_get_detail(){
      // Create a z_client
      ZClient zClient = new ZClient(getConfiguration());

      // create an transaction revenue schedule resource manager
      RevenueScheduleManager crsManager = new RevenueScheduleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         crsManager.getDetailByNumber(SAMPLE_TRANSACTION_REVENUE_SCHEDULE_KEY);
         crsManager.getDetailByInvoiceItem(SAMPLE_INVOICE_ITEM_KEY);
         crsManager.getDetailByInvoiceItemAdjustment(SAMPLE_INVOICE_ITEM_ADJ_KEY);
         crsManager.getDetailsByCharge(SAMPLE_CHARGE_KEY);
      }
   }

   @Test
   public void test_change_basic(){
      ZClient zClient = new ZClient(getConfiguration());

      // create an transaction revenue schedule resource manager
      RevenueScheduleManager crsManager = new RevenueScheduleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         crsManager.updateBasicInfo(SAMPLE_TRANSACTION_REVENUE_SCHEDULE_KEY);
      }
   }

   @Test
   public void test_distribute_revenue_ratably(){
      ZClient zClient = new ZClient(getConfiguration());

      // create an transaction revenue schedule resource manager
      RevenueScheduleManager crsManager = new RevenueScheduleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         crsManager.distributeRevenueRatably(SAMPLE_TRANSACTION_REVENUE_SCHEDULE_KEY);
      }
   }

   @Test
   public void test_distribute_revenue_manually(){
      ZClient zClient = new ZClient(getConfiguration());

      // create an transaction revenue schedule resource manager
      RevenueScheduleManager crsManager = new RevenueScheduleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         Map<String, String> distributionItems = new HashMap<String, String>();
         distributionItems.put(SAMPLE_AP1_NAME, "2.35");
         distributionItems.put(SAMPLE_AP2_NAME, "-2.35");
         crsManager.distributeRevenueManually(SAMPLE_TRANSACTION_REVENUE_SCHEDULE_KEY, distributionItems);
      }
   }

   @Test
   public void test_create_rs_by_charge() {
      ZClient zClient = new ZClient(getConfiguration());

      // create an transaction revenue schedule resource manager
      RevenueScheduleManager crsManager = new RevenueScheduleManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         Map<String, String> distributionItems = new HashMap<String, String>();
         distributionItems.put(SAMPLE_AP1_NAME, "100");
         distributionItems.put(SAMPLE_AP2_NAME, "200");
         crsManager.createRSByCharge(SAMPLE_CHARGE_KEY, distributionItems);
      }
   }
}
