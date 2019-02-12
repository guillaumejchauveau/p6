package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.Collection;

/**
 * The product of a reaction that will always keep the first element.
 */
public class ReactantsReactionProduct extends ReactionProduct {
  private final Collection<Integer> elementsToChoose;

  public ReactantsReactionProduct(Collection<Integer> elementsToChoose) {
    this.elementsToChoose = elementsToChoose;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void react(Collection<Element> reactants, Cell cell) {
    //cell.
  }
}
