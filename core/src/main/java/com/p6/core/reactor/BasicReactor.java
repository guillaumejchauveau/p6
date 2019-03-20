package com.p6.core.reactor;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A single-threaded reactor.
 */
public class BasicReactor extends Reactor {
  /**
   * {@inheritDoc}
   */
  @Override
  public void iterate(Cell cell, Integer iterationTarget, Integer stabilityTarget) {
    Logger logger = LogManager.getLogger();
    int stability = 0;

    for (int iteration = 0; iteration < iterationTarget; iteration++) {
      if (cell.getElementsCount() <= 1 || cell.isDissolved()) {
        logger.debug("Solution cannot react further");
        break;
      }
      if (stability >= stabilityTarget) {
        logger.debug("Solution reached target stability");
        break;
      }

      Element x = cell.chooseElement();
      Element y = cell.chooseElement();

      boolean reactionOccurred = false;
      for (ReactionPipeline pipeline : cell.getPipelines()) {
        List<Element> elements = new ArrayList<>();
        elements.add(x);
        elements.add(y);
        if (pipeline.handle(elements, cell)) {
          reactionOccurred = true;
          stability = 0;
          break;
        }
      }

      if (!reactionOccurred) {
        cell.addElement(x);
        cell.addElement(y);
        stability++;
      }
    }
  }
}
