package com.example.configuration.application;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Application {
  private @NotBlank String id;
}
