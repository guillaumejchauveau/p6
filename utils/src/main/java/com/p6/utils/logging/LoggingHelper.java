package com.p6.utils.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class LoggingHelper {
  private static Boolean isConfigured = false;

  /**
   * Configures Log4J application-wide.
   *
   * @param level The minimum logging level to print
   */
  public static void configureLoggingFramework(Level level) {
    if (LoggingHelper.isConfigured) {
      return;
    }

    String pattern = "%d{HH:mm:ss,SS} ";
    pattern += "%highlight{%-5level}{";
    pattern += "FATAL=red bright, ERROR=red, WARN=yellow, INFO=blue, DEBUG=cyan, TRACE=white} ";
    pattern += "%style{%c{-2}}{bright}: %msg{ansi}%n%style{%throwable}{white}";

    ConfigurationBuilder<BuiltConfiguration> builder =
        ConfigurationBuilderFactory.newConfigurationBuilder();
    AppenderComponentBuilder appenderBuilder = builder.newAppender("StdERR", "CONSOLE")
                                                      .addAttribute("target",
                                                        ConsoleAppender.Target.SYSTEM_ERR);
    LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                                           .addAttribute("pattern", pattern);
    appenderBuilder.add(layout);
    builder.add(appenderBuilder);
    builder.add(builder.newRootLogger(level).add(builder.newAppenderRef("StdERR")));

    Configurator.initialize(builder.build());
    Logger logger = LogManager.getLogger();
    logger.trace("Logging framework configured");
    LoggingHelper.isConfigured = true;
  }
}
