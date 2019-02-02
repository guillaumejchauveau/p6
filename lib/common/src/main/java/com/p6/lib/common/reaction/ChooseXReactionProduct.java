package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Element;
import java.util.ArrayList;
import java.util.Collection;

public class ChooseXReactionProduct extends ReactionProduct {
  @Override
  public Collection<Element> getProducts(Element x, Element y) {
    Collection<Element> products = new ArrayList<>();
    products.add(x);
    return products;
  }
}
