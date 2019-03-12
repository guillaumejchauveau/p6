package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Element;

/**
 *
 */
public class DissolveCellReactionProduct extends ReactionProduct {
  /**
   * {@inheritDoc}
   */
  @Override
  public void react(Element x, Element y) {
    this.getCell().dissolve();
  }
}
