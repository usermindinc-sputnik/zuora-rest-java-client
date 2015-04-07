package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class JournalEntryManagerTest extends BaseZuoraApiTest{
	   static final String SAMPLE_JOURNAL_ENTRY_KEY = "JE-00000014";
	   static final String SAMPLE_JOURNAL_RUN_KEY = "JR-00000002";

	   @Test
	   public void test_change_basic(){
	      ZClient zClient = new ZClient(getConfiguration());

	      // create a journal entry resource manager
	      JournalEntryManager jeManager = new JournalEntryManager(zClient);

	      // Connect to the End Point using default tenant's credentials
	      if (new ConnectionManager().isConnected(zClient)) {
	    	  jeManager.updateBasicInfo(SAMPLE_JOURNAL_ENTRY_KEY);
	      }
	   }

	   @Test
	   public void test_create(){
	      ZClient zClient = new ZClient(getConfiguration());

	      // create a journal entry resource manager
	      JournalEntryManager jeManager = new JournalEntryManager(zClient);

	      // Connect to the End Point using default tenant's credentials
	      if (new ConnectionManager().isConnected(zClient)) {
	    	  jeManager.createJournalEntry();
	      }
	   }

	   @Test
	   public void test_get_journal_entries_by_journal_run_number(){
		      ZClient zClient = new ZClient(getConfiguration());

		      // create a journal entry resource manager
		      JournalEntryManager jeManager = new JournalEntryManager(zClient);

		      // Connect to the End Point using default tenant's credentials
		      if (new ConnectionManager().isConnected(zClient)) {
		    	  jeManager.getJournalEntriesByJournalRunNumber(SAMPLE_JOURNAL_RUN_KEY);
		      }
	   }

	   @Test
      public void test_get_journal_entry_by_journal_entry_number(){
            ZClient zClient = new ZClient(getConfiguration());

            // create a journal entry resource manager
            JournalEntryManager jeManager = new JournalEntryManager(zClient);

            // Connect to the End Point using default tenant's credentials
            if (new ConnectionManager().isConnected(zClient)) {
              jeManager.getJournalEntryByJournalEntryNumber(SAMPLE_JOURNAL_ENTRY_KEY);
            }
      }
}
