package com.example;

import com.example.configuration.ExampleProperties;
import com.example.configuration.application.StoreApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.annotation.Validated;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = ConfigurationPropertiesTest.ConfigurationPropertiesBinding.class,
  properties = {
    "acme.applications.foo.type = store",
    "acme.applications.foo.entity-filters.products[0].pattern = exclude_.+",
    "acme.applications.foo.entity-filters.products[0].include = false"
  }
)
@EnableConfigurationProperties
public class ConfigurationPropertiesTest {
  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testSpringConfigurationPropertiesBindingWorks() {
    // Set the id property which would be deferred from the key by the ApplicationBinder
    System.setProperty("acme.applications.foo.id", "foo");

    Assert.assertEquals(
      "StoreApplication(super=Application(id=foo), " +
        "entityFilters={PRODUCTS=[EntityFilter(include=false, pattern=exclude_.+)]})",
      applicationContext.getBean(StoreApplication.class).toString()
    );
  }

  @Service
  public static class ConfigurationPropertiesBinding {
    @Bean
    @ConfigurationProperties(prefix = "acme.applications.foo")
    @Scope("prototype")
    @Validated
    public StoreApplication createStore() {
      return new StoreApplication();
    }
  }
}
