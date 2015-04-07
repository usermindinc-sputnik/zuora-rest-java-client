package com.zuora.sdk.samples;

import com.usermind.integrations.common.config.Configuration;
import com.usermind.integrations.common.config.ConfigurationBuilder;
import org.junit.Before;

public abstract class BaseZuoraApiTest {

  private Configuration configuration;

  @Before
  public void initConfiguration() {
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

    // SDK specific properties - ALL PROPERTIES must be PRESENT
    configurationBuilder.put("http.user.agent", "z_sdk_v1.0");
    configurationBuilder.put("http.connect.timeout", "30000");
    configurationBuilder.put("http.receive.timeout", "30000");
    configurationBuilder.put("http.max.connection.pool.size", "30");

    // Zuora REST API specific properties - ALL PROPERTIES MUST BE PRESENT
    configurationBuilder.put("rest.api.endpoint", "https://apisandbox-api.zuora.com/rest");
    configurationBuilder.put("rest.api.version", "v1");

    // Installation specific properties - ALL PROPERTIES MUST BE PRESENT
    configurationBuilder.put("default.tenant.user.id", "daniel+tune@usermind.com");
    configurationBuilder.put("default.tenant.password", "Usermind2015!");
    configurationBuilder.put("proxy.used", "false");
    configurationBuilder.put("proxy.url", "http://www.mycompany.com:8000");
    configurationBuilder.put("proxy.auth", "false");
    configurationBuilder.put("proxy.user", "bigbrother");
    configurationBuilder.put("proxy.password", "bigbrother");
    configurationBuilder.put("api.trace", "true");
    configurationBuilder.put("ssl.verify.peer", "false");

    configuration = configurationBuilder.toConfiguration();
  }

  public Configuration getConfiguration() {
    return configuration;
  }
}
