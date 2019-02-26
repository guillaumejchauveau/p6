package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;

/**
 * The product of a reaction that will always keep the first element.
 */
public class ReactantsReactionProduct extends ReactionProduct {
  private final Boolean chooseX;

  public ReactantsReactionProduct(Boolean chooseX) {
    this.chooseX = chooseX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void react(Element x, Element y, Cell cell) {
    cell.addElement((this.chooseX) ? x : y);
  }
}
