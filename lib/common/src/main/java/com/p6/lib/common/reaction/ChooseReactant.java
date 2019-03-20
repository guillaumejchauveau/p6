package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionPipelineStep;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the first (left) or second (right) element of the pipeline. Intended to be used as the
 * first step to modify the pipeline (with the reactants selected by the reactor).
 */
public class ChooseReactant implements ReactionPipelineStep {
  private final Element.Side elementSide;

  /**
   * Creates a reactant choice step.
   *
   * @param elementSide The side of the element
   */
  public ChooseReactant(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Element> handle(List<Element> inputElements, Cell cell) {
    List<Element> output = new ArrayList<>();
    output.add(inputElements.get((this.elementSide == Element.Side.LEFT) ? 0 : 1));
    return output;
  }
}
