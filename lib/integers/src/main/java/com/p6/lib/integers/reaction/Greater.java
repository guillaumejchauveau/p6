package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;

/**
 * A condition that will make two integer elements react if the first is greater
 * than the last.
 */
public class GreaterThanReactionCondition extends IntegerReactionCondition {
  @Override
  Boolean integerTest(IntegerElement x, IntegerElement y) {
    return x.evaluate() > y.evaluate();
  }
}
