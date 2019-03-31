package demos.java;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.logging.LoggingHelper;
import demos.java.CharElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;


public class CoreAPI {
  public static void main(String[] args) throws InterruptedException {
    LoggingHelper.configureLoggingFramework(Level.DEBUG);

    Cell cell = new Cell();
    ReactionPipeline pipeline = new ReactionPipeline();
    pipeline.addStep((inputElements, cell1) -> {
      List<Element> output = new ArrayList<>();
      output.add(inputElements.get(0));
      return output;
    });
    cell.addPipeline(pipeline);

    cell.addAllElements(() -> {
      List<Element> elements = new ArrayList<>();
      for (char c = 'a'; c <= 'z'; c++) {
        elements.add(new CharElement(c));
      }
      return elements;
    });

    ReactorCoordinator coordinator = new ReactorCoordinator(cell, 10, 1);
    coordinator.run();
    coordinator.block();
    System.out.println(cell.getElements());
  }
}
