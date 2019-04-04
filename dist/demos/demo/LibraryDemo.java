package demo;

import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.lib.SolutionBuilder;
import com.p6.utils.logging.LoggingHelper;
import java.util.ArrayList;
import org.apache.logging.log4j.Level;


public class LibraryDemo {
  public static void main(String[] args) throws InterruptedException {
    LoggingHelper.configureLoggingFramework(Level.DEBUG);

    var registry = new LibraryRegistry();

    var solutionBuilder = new SolutionBuilder(registry);
    var rootCell = solutionBuilder.createCell();
    solutionBuilder.createPipeline()
                   .addStep("chooseAChar")
                   .addElement("lotsOfChars")
                   .createPipeline()
                   .addStep("sortInt", Element.Side.LEFT)
                   .addStep("choose", Element.Side.LEFT)
                   .addElement("range", "0", "10", "1")
                   .sealCell();

    System.out.println(rootCell.getElements());
    var coordinator = new ReactorCoordinator(solutionBuilder.getSolution(), 20, 10);
    coordinator.run();
    coordinator.block();
    System.out.println(rootCell.getElements());
  }
}
