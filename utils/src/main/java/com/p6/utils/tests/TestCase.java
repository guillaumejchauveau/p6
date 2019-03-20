package com.p6.utils.tests;

import com.p6.utils.logging.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base class for test cases.
 */
public abstract class TestCase {
  /**
   * Logger used for debugging.
   */
  protected Logger logger;

  public TestCase() {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    this.logger = LogManager.getLogger();
  }
}
