package com.p6.lib.common.reaction;

import com.p6.core.reaction.ReactionProduct;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;

public class InjectInSubCellReactionProduct extends ReactionProduct {
  private final Cell subCell;

  public InjectInSubCellReactionProduct(Cell subCell) {
    this.subCell = subCell;
  }

  @Override
  public void react(Element x, Element y) {
    //this.getCell().inject(, this.subCell);
  }
}
