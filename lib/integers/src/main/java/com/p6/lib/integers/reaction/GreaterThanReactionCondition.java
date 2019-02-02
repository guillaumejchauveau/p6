package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;

public class GreaterThanReactionCondition extends ReactionCondition {
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
