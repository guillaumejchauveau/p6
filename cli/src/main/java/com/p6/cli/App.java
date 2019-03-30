package com.p6.cli;

import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.CellParser;
import com.p6.parser.InvalidSyntaxException;
import com.p6.utils.logging.LoggingHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

/**
 * A simple application that allows the user to load a P6 program from a file and execute it.
 * The resulting cells can then be exported in an output file. Launch the app with the flag "-h"
 * for usage information.
 */
@CommandLine.Command(name = "cli", mixinStandardHelpOptions = true)
public class App implements Callable<Boolean> {
  /**
   * The path of the file containing the P6 program. The value is set by the PicoCLI framework.
   */
  @CommandLine.Parameters(index = "0", arity = "1", paramLabel = "PATH",
      description = "The path of the P6 program to run")
  private String path;

  /**
   * The iteration target to use when configuring the reactors.
   * The value is set by the PicoCLI framework.
   *
   * @see com.p6.core.reactor.Reactor
   */
  @CommandLine.Parameters(index = "1", arity = "1", paramLabel = "ITERATION_TARGET",
      description = "The iteration target for the reactors")
  private Integer iterationTarget;

  /**
   * The stability target to use when configuring the reactors.
   * The value is set by the PicoCLI framework.
   *
   * @see com.p6.core.reactor.Reactor
   */
  @CommandLine.Parameters(index = "2", arity = "1", paramLabel = "STABILITY_TARGET",
      description = "The stability target for the reactors")
  private Integer stabilityTarget;

  /**
   * Indicates if the application should display details about the reactions.
   * The logging rate is slowed using a {@link com.p6.utils.logging.plugins.SleepFilter}.
   * The value is set by the PicoCLI framework.
   */
  @CommandLine.Option(names = {"-v", "--verbose"}, defaultValue = "false",
      description = "Display reaction status slowly. Takes precedence over 'quiet'")
  private Boolean verbose;

  /**
   * Indicates if the application should only display warnings or errors.
   * The value is set by the PicoCLI framework.
   */
  @CommandLine.Option(names = {"-q", "--quiet"}, defaultValue = "false",
      description = "Only show warnings")
  private Boolean quiet;

  /**
   * Indicates in which file the results should be saved.
   * The value is set by the PicoCLI framework.
   */
  @CommandLine.Option(names = {"-o", "--output"}, arity = "1", paramLabel = "<file>",
      description = "Output details about the cells' final state to the specified file")
  private String output;

  public static void main(String[] args) {
    System.exit(CommandLine.call(new App(), args) ? 0 : 1);
  }

  /**
   * {@inheritDoc}
   *
   * @return Indicates if the app ran normally (true) or if an error has been encountered (false)
   */
  public Boolean call() {
    // Configures the loggers depending of the user's configuration.
    LoggingHelper.configureLoggingFramework(
        this.verbose ? Level.DEBUG : (this.quiet ? Level.WARN : Level.INFO));
    final Logger logger = LogManager.getLogger();

    LibraryRegistry registry = new LibraryRegistry();

    // Loads the P6 program specified by the user.
    logger.info("Loading file '" + this.path + "'");
    String source;
    try {
      source = Files.readString(Paths.get(this.path));
    } catch (IOException e) {
      if (!this.verbose) {
        e.setStackTrace(new StackTraceElement[]{});
      }
      logger.error("Error while loading file '" + this.path + "'", e);
      return false;
    }

    // Parses the program.
    Cell cell;
    try {
      CellParser cellParser = new CellParser(source);
      cell = cellParser.create(registry);
    } catch (InvalidSyntaxException | IllegalArgumentException e) {
      if (!this.verbose) {
        e.setStackTrace(new StackTraceElement[]{});
      }
      logger.error("Error while loading data from file '" + this.path + "'", e);
      return false;
    }

    // Runs the program.
    ReactorCoordinator reactorCoordinator = new ReactorCoordinator(cell, this.iterationTarget,
        this.stabilityTarget);
    reactorCoordinator.run();

    // Waits for all the reactors to finish.
    try {
      while (Thread.activeCount() != 1) {
        Thread.sleep(1000);
        if (reactorCoordinator.getState() == ReactorCoordinator.State.FAILED) {
          logger.error("A reactor entered FAILED state");
          return false;
        }
      }
    } catch (InterruptedException e) {
      if (!this.verbose) {
        e.setStackTrace(new StackTraceElement[]{});
      }
      logger.fatal("Unexpected main thread interruption", e);
      return false;
    }

    // Displays the results or saves them in a file.
    if (this.output != null) {
      String export = this.exportCell(cell);
      try {
        FileWriter fileWriter = new FileWriter(new File(this.output));
        fileWriter.write(export);
        fileWriter.close();
        logger.info("Results saved in file '" + this.output + "'");
      } catch (IOException e) {
        if (!this.verbose) {
          e.setStackTrace(new StackTraceElement[]{});
        }
        logger.error("Could not save results to file '" + this.output + "'", e);
        return false;
      }
    } else if (!this.quiet) {
      this.printCell(cell, 0);
    }
    return true;
  }

  /**
   * Displays a cell's elements and it's sub-cells.
   * Note that dissolved cells are no longer in the cell tree.
   *
   * @param cell  The cell to display
   * @param level The position in the cell tree
   */
  private void printCell(Cell cell, Integer level) {
    System.out.println("  ".repeat(level) + cell.getName());
    System.out.println("  ".repeat(level) + cell.getElements());
    System.out.println();
    for (Cell subCell : cell.getSubCells()) {
      printCell(subCell, level + 1);
    }
  }

  /**
   * Generates a string used to save a cell's state.
   * The string has the form:
   * <br>
   * Cell_name Element_class Element_value<br>
   * .<br>
   * .<br>
   * Sub_cell_name Element_class Element_value<br>
   * .<br>
   * .<br>
   * ---<br>
   * Next_cell_name Element_class Element_value<br>
   * .<br>
   * .<br>
   * ---<br>
   * Note that dissolved cells are no longer in the cell tree.
   *
   * @param cell The cell to export
   * @return The string representing the cell
   */
  private String exportCell(Cell cell) {
    StringBuilder builder = new StringBuilder();
    for (Element element : cell.getElements()) {
      builder.append(cell.getName()).append(" ");
      builder.append(element.getClass().getName()).append(" ");
      builder.append(element.toString()).append("\n");
    }
    for (Cell subCell : cell.getSubCells()) {
      builder.append(this.exportCell(subCell));
    }
    builder.append("---\n");
    return builder.toString();
  }
}
