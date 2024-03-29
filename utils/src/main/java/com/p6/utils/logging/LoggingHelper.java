package com.p6.utils.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;

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
    PluginManager.addPackage("com.p6.utils.logging.plugins");

    var pattern = "%d{HH:mm:ss} %style{%thread}{underline} ";
    pattern += "%highlight{%-5level}{";
    pattern += "FATAL=red bright, ERROR=red, WARN=yellow, INFO=blue, DEBUG=cyan, TRACE=white} ";
    pattern += "%style{%c{-2}}{bright}: %msg{ansi}%n%style{%throwable}{white}";

    var builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    var appenderBuilder = builder.newAppender("StdERR", "CONSOLE")
                                 .addAttribute("target", ConsoleAppender.Target.SYSTEM_ERR);
    var layout = builder.newLayout("PatternLayout")
                        .addAttribute("pattern", pattern);
    var sleepFilter = builder.newFilter("SleepFilter", "NEUTRAL", "NEUTRAL")
                             .addAttribute("time", 500);
    appenderBuilder.add(layout);
    appenderBuilder.add(sleepFilter);
    builder.add(appenderBuilder);
    builder.add(builder.newRootLogger(level).add(builder.newAppenderRef("StdERR")));

    Configurator.initialize(builder.build());
    var logger = LogManager.getLogger();
    logger.trace("Logging framework configured");
    LoggingHelper.isConfigured = true;
  }
}
