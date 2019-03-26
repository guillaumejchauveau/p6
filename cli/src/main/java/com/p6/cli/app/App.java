package com.p6.cli.app;

import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.CellParser;
import com.p6.utils.logging.LoggingHelper;
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
  public static void main(String[] args) throws Exception {
    LoggingHelper.configureLoggingFramework(Level.ALL);
    final Logger logger = LogManager.getLogger();
    LibraryRegistry registry = new LibraryRegistry();

    String source = Files.readString(Paths.get("test.txt"));

    CellParser cellParser = new CellParser(source);
    Cell cell = cellParser.create(registry);

    ReactorCoordinator reactorCoordinator = new ReactorCoordinator(cell, 1000, 100);
    reactorCoordinator.run();
    while (Thread.activeCount() != 1) {
      Thread.sleep(100);
    }
    logger.debug(cell.getElements());
  }
}
