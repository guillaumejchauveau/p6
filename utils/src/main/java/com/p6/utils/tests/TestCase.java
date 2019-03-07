package com.p6.utils.tests;

import com.p6.utils.logging.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TestCase {
  protected Logger logger;

  public TestCase() {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    this.logger = LogManager.getLogger();
  }
}
