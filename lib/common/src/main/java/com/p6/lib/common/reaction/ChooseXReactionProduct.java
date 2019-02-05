package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The product of a reaction that will always keep the first element.
 */
public class ChooseXReactionProduct extends ReactionProduct {
  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Element> getProducts(Element x, Element y) {
    Collection<Element> products = new ArrayList<>();
    products.add(x);
    return products;
  }
}
