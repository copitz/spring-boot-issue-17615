package com.example.configuration.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreApplication extends Application {
  private Map<Entity, Collection<EntityFilter>> entityFilters;

}
