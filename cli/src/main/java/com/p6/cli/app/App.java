package com.p6.cli.app;

import com.p6.core.solution.SolutionConfigurator;
import com.p6.utils.LoggingHelper;
import java.util.ServiceLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point.
 */
public class App {
  /**
   * Displays the app's message.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    Logger logger = LogManager.getLogger();

    ServiceLoader<SolutionConfigurator> loader = ServiceLoader.load(SolutionConfigurator.class);
    for (SolutionConfigurator configurator : loader) {
      logger.debug(configurator);
    }
  }
}
