package com.p6.cli.app;

import com.p6.core.solution.Cell;
import com.p6.lib.LibraryRegistry;
import com.p6.utils.logging.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point.
 */
public class App {
  /**
   * A test script.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    final Logger logger = LogManager.getLogger();
    LibraryRegistry registry = new LibraryRegistry();
    logger.debug(registry.getElementGeneratorNames());
    logger.debug(registry.getReactionPipelineStepNames());
    logger.debug(registry.createReactionPipelineStep("clear"));
  }
}
