package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Sort implements ReactionPipelineStep {
  private final Element.Side elementSide;

  /**
   * @param elementSide
   */
  public Sort(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<Element> outputElements = new ArrayList<>(inputElements);
    outputElements.sort((Element o1, Element o2) -> {
      Object x1 = o1.evaluate();
      Object x2 = o2.evaluate();
      if (x1 instanceof Comparable) {
        return ((Comparable) x1).compareTo(x2) * (this.elementSide == Element.Side.LEFT ? 1 : -1);
      } else {
        throw new ClassCastException();
      }
    });
    return outputElements;
  }
}
