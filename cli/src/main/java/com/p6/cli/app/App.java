package com.p6.cli.app;

import com.p6.core.solution.SolutionConfigurator;
import com.p6.utils.Logging;
import java.util.ServiceLoader;
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
    Logging.configureLoggingFramework();
    Logger logger = LogManager.getLogger();
    logger.error("Test");

    ServiceLoader<SolutionConfigurator> loader = ServiceLoader.load(SolutionConfigurator.class);
    for (SolutionConfigurator configurator : loader) {
      System.out.println(configurator);
    }
  }
}
