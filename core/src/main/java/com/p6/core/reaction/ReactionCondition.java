package com.p6.core.reaction;

import com.p6.core.solution.Element;

public abstract class ReactionCondition {
  public abstract Boolean test(Element x, Element y);
}
