package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;

/**
 * An object that determines the product of the reaction between two elements.
 */
public abstract class ReactionProduct {
  /**
   * Creates elements produced by the reaction between the given elements.
   *
   * @param x    The first element
   * @param y    The second element
   * @param cell The cell in which the reaction occurs
   */
  public abstract void react(Element x, Element y, Cell cell);
}
