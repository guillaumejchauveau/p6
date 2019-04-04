package com.p6.lib.integers.reaction;

import com.p6.core.solution.Element;
import com.p6.lib.common.reaction.Sort;

public class SortIntegers extends Sort<Integer> {
  /**
   * Creates a sort step for integers.
   *
   * @param elementSide The side of the "greatest" element
   */
  public SortIntegers(Element.Side elementSide) {
    super(elementSide);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Class<Integer> getComparisonClass() {
    return Integer.class;
  }

  @Override
  public String toString() {
    return "SortIntegers";
  }
}
