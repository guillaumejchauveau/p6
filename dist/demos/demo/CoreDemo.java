package demo;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.logging.LoggingHelper;
import demo.CharElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;


public class CoreDemo {
  public static void main(String[] args) throws InterruptedException {
    LoggingHelper.configureLoggingFramework(Level.DEBUG);

    var cell = new Cell();
    var pipeline = new ReactionPipeline();
    pipeline.addStep((inputElements, cell1) -> {
      var output = new ArrayList<Element>();
      output.add(inputElements.get(0));
      return output;
    });
    cell.addPipeline(pipeline);

    cell.addAllElements(() -> {
      var elements = new ArrayList<Element>();
      for (var c = 'a'; c <= 'z'; c++) {
        elements.add(new CharElement(c));
      }
      return elements;
    });

    var coordinator = new ReactorCoordinator(cell, 10, 1);
    coordinator.run();
    coordinator.block();
    System.out.println(cell.getElements());
  }
}
