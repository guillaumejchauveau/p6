package com.p6.gui.app;

import com.p6.utils.LoggingHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;
import java.util.ArrayList;

/**
 * Entry point.
 */
public class App {
  /**
   * A test script.
   * @param args System args
   */
  public static void main(String[] args) {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    Logger logger = LogManager.getLogger();

    Window window1 = new Window();

  }
}
