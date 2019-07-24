package com.example;

import com.example.configuration.ExampleProperties;
import com.example.configuration.application.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = ExampleProperties.class,
  properties = {
    "acme.applications.foo.type = store",
    "acme.applications.foo.entity-filters.products[0].pattern = exclude_.+",
    "acme.applications.foo.entity-filters.products[0].include = false"
  }
)
@EnableConfigurationProperties
public class ApplicationsBinderTest {
  @Autowired
  private ExampleProperties acmeProperties;

  /**
   * Will fail with following message:
   * <code>
   *   Failed to bind properties under 'foo.entity-filters' to java.util.Map<com.example.configuration.application.Entity, java.util.Collection<com.example.configuration.application.EntityFilter>>
   *   org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'foo.entity-filters' to java.util.Map<com.example.configuration.application.Entity, java.util.Collection<com.example.configuration.application.EntityFilter>>
   * </code>
   */
  @Test(expected = BindException.class)
  public void testManualFailingBinding() {
    ApplicationsBinder.createApplications(
      acmeProperties.getApplications(),
      false // Do NOT fix problematic property names
    );
  }

  /**
   * Will not fail and bind correctly
   */
  @Test
  public void testManualWorkingBinding() {
    Collection<Application> applications = ApplicationsBinder.createApplications(
      acmeProperties.getApplications(),
      true // DO fix problematic property names
    );

    // The binding was successful, check if the result is as expected:
    Assert.assertEquals(
      "[StoreApplication(super=Application(id=foo), " +
        "entityFilters={PRODUCTS=[EntityFilter(include=false, pattern=exclude_.+)]})]",
      applications.toString()
    );
  }
}
