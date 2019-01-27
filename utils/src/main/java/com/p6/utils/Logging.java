package com.p6.utils;

import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class Logging {
  /**
   * Configures Log4J application-wide.
   */
  public static void configureLoggingFramework() {
    ConfigurationBuilder<BuiltConfiguration> builder =
      ConfigurationBuilderFactory.newConfigurationBuilder();
    Configurator.initialize(builder.build());
  }
}
