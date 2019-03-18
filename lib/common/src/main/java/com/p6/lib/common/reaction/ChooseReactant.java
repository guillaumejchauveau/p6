package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * The product of a reaction that will always keep the first element.
 */
public class ChooseReactant implements ReactionPipelineStep {
  private final Element.Side elementSide;

  /**
   * @param elementSide
   */
  public ChooseReactant(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<Element> output = new ArrayList<>();
    output.add(inputElements.get((this.elementSide == Element.Side.LEFT) ? 0 : 1));
    return output;
  }
}
