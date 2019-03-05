package com.p6.lib.integers.reaction;

import com.p6.lib.integers.IntegerElement;

public class EqualsReactionCondition extends IntegerReactionCondition {
  @Override
  Boolean integerTest(IntegerElement x, IntegerElement y) {
    return x.evaluate().equals(y.evaluate());
  }
}
