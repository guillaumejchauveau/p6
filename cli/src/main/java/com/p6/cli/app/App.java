package com.p6.cli.app;

import com.p6.lib.LibraryRegistry;
import com.p6.parser.CellParser;
import com.p6.parser.InvalidSyntaxException;
import com.p6.utils.logging.LoggingHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
  public static void main(String[] args) throws InvalidSyntaxException, IOException {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    final Logger logger = LogManager.getLogger();

    String source = Files.readString(Paths.get("test.txt"));

    LibraryRegistry registry = new LibraryRegistry();
    CellParser cellParser = new CellParser(source);
    logger.debug(cellParser.create(registry));
  }
}
