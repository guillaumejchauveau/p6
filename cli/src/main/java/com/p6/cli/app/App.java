package com.p6.cli.app;

import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.CellParser;
import com.p6.parser.InvalidSyntaxException;
import com.p6.utils.logging.LoggingHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

/**
 * Entry point.
 */
@CommandLine.Command()
public class App implements Callable<Boolean> {
  @CommandLine.Parameters(index = "0")
  private String path;
  @CommandLine.Parameters(index = "1")
  private Integer iterationTarget;
  @CommandLine.Parameters(index = "2")
  private Integer stabilityTarget;

  @CommandLine.Option(names = {"-v", "--verbose"})
  private Boolean verbose;

  @CommandLine.Option(names = {"-e", "--export"})
  private Boolean export;

  public static void main(String[] args) {
    System.exit(CommandLine.call(new App(), args) ? 0 : 1);
  }

  private App() {
    this.verbose = false;
    this.export = false;
  }

  public Boolean call() {
    LoggingHelper.configureLoggingFramework(this.verbose ? Level.DEBUG : Level.INFO);
    final Logger logger = LogManager.getLogger();
    LibraryRegistry registry = new LibraryRegistry();

    logger.info("Loading file '" + this.path + "'");
    String source;
    try {
      source = Files.readString(Paths.get(this.path));
    } catch (IOException e) {
      logger.error("Error while loading file '" + this.path + "'", e);
      return false;
    }

    Cell cell;
    try {
      CellParser cellParser = new CellParser(source);
      cell = cellParser.create(registry);
    } catch (InvalidSyntaxException e) {
      logger.error("Error while loading data from file '" + this.path + "'", e);
      return false;
    }

    ReactorCoordinator reactorCoordinator = new ReactorCoordinator(cell, this.iterationTarget,
      this.stabilityTarget);
    reactorCoordinator.run();

    try {
      while (Thread.activeCount() != 1) {
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      logger.fatal("Unexpected main thread interruption", e);
      return false;
    }
    if (this.export) {
      this.exportCell(cell);
    } else {
      this.printCell(cell, 0);
    }
    return true;
  }

  /**
   *
   */
  private void printCell(Cell cell, Integer level) {
    System.out.println("  ".repeat(level) + cell.getName());
    System.out.println("  ".repeat(level) + cell.getElements());
    System.out.println();
    for (Cell subCell : cell.getSubCells()) {
      printCell(subCell, level + 1);
    }
  }

  private void exportCell(Cell cell) {
    for (Element element : cell.getElements()) {
      System.out.print(cell.getName() + " ");
      System.out.print(element.getClass().getName() + " ");
      System.out.println(element.toString());
    }
    for (Cell subCell : cell.getSubCells()) {
      this.exportCell(subCell);
    }
    System.out.println("---");
  }
}
