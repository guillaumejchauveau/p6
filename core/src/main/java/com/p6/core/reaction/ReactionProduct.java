package com.p6.core.reaction;

import com.p6.core.solution.Element;
import java.util.Collection;

public abstract class ReactionProduct {
  public abstract Collection<Element> getProducts(Element x, Element y);
}
