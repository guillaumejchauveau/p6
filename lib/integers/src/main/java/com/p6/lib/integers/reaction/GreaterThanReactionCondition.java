package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;

/**
 * A condition that will make two integer elements react if the first is greater
 * than the last.
 */
public class GreaterThanReactionCondition extends ReactionCondition {
  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean test(Element x, Element y) {
    if (x instanceof IntegerElement && y instanceof IntegerElement) {
      IntegerElement x1 = (IntegerElement) x;
      IntegerElement y1 = (IntegerElement) y;
      return x1.evaluate() > y1.evaluate();
    }
    return false;
  }
}
