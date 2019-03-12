package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Element;

/**
 * The product of a reaction that will always keep the first element.
 */
public class ReactantsReactionProduct extends ReactionProduct {
  private final Element.Side elementSide;

  /**
   * @param elementSide
   */
  public ReactantsReactionProduct(Element.Side elementSide) {
    this.elementSide = elementSide;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void react(Element x, Element y) {
    this.getCell().addElement((this.elementSide == Element.Side.LEFT) ? x : y);
  }
}
