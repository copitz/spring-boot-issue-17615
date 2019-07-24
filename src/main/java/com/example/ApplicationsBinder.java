package com.example;

import com.example.configuration.application.Application;
import com.example.configuration.application.StoreApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationsBinder {
  public static Application createApplication(String id, Map<String, String> properties, boolean fixProblematicPropertyNames) {
    // Deduce the concrete class from the type property (only "store" possible in this example)
    String type = properties.get("type");
    Assert.hasLength(type, "Type is required");
    if (!type.equals("store")) {
      throw new IllegalStateException("Unsupported application type: " + type);
    }
    Class<? extends Application> cls = StoreApplication.class;

    // Create a prefixed properties map
    String prefix = "foo";
    Map<String, String> prefixedProperties = new LinkedHashMap<>();
    prefixedProperties.put(prefix + ".id", id);
    properties.forEach((k, v) -> {
      String prefixedKey = prefix + "." + k;
      if (fixProblematicPropertyNames) {
        prefixedKey = prefixedKey.replaceAll("\\.([0-9]+)(\\.|$)", "[$1]$2");
      }
      prefixedProperties.put(prefixedKey, v);
    });

    // Bind properties to the concrete class
    Binder binder = new Binder(new MapConfigurationPropertySource(prefixedProperties));
    return binder.bind(prefix, Bindable.of(cls)).get();
  }

  public static Collection<Application> createApplications(Map<String, Map<String, String>> applicationsProperties, boolean fixProblematicPropertyNames) {
    return applicationsProperties.entrySet().stream()
      .map(entry -> createApplication(entry.getKey(), entry.getValue(), fixProblematicPropertyNames))
      .collect(Collectors.toSet());
  }
}
