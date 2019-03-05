package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;

abstract class IntegerReactionCondition extends ReactionCondition {
  @Override
  public Boolean test(Element x, Element y) {
    if (x instanceof IntegerElement && y instanceof IntegerElement) {
      return this.integerTest((IntegerElement) x, (IntegerElement) y);
    }
    return false;
  }

  abstract Boolean integerTest(IntegerElement x, IntegerElement y);
}
