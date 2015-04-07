package com.zuora.sdk.samples;

import com.zuora.sdk.lib.ZClient;
import org.junit.Test;

public class AccountManagerTest extends BaseZuoraApiTest {
   static final String SAMPLE_ACCOUNT_KEY = "A00001069";



   @Test
   public void test_account_manager(){
      // Create a z_client
      ZClient zClient = new ZClient(getConfiguration());

      // create an account resource manager
      AccountManager accountManager = new AccountManager(zClient);

      // Connect to the End Point using default tenant's credentials
      // and practice APIs
      if (new ConnectionManager().isConnected(zClient)) {
        accountManager.getSummary(SAMPLE_ACCOUNT_KEY);
        accountManager.getDetails(SAMPLE_ACCOUNT_KEY);
        String accountNumber = accountManager.create();
        if (accountNumber != null) {
          accountManager.update(accountNumber);
          accountManager.createWithSubscription();
        }
      }

   }
}
