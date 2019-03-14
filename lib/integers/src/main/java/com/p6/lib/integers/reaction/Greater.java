package com.p6.lib.integers.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A condition that will make two integer elements react if the first is greater
 * than the last.
 */
public class Greater extends IntegerReactionPipelineStep {
  private final Element.Side elementSide;

  /**
   * @param elementSide
   */
  public Greater(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  List<IntegerElement> integerTest(List<IntegerElement> inputElements, Cell cell) {
    List<IntegerElement> outputElements = new ArrayList<>(inputElements);
    outputElements.sort((IntegerElement o1, IntegerElement o2) ->
      o1.evaluate().compareTo(o2.evaluate()) * (this.elementSide == Element.Side.LEFT ? 1 : -1));
    return outputElements;
  }
}
