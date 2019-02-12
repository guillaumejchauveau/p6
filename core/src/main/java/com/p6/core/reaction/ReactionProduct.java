package com.p6.core.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import java.util.Collection;

/**
 * An object that determines the product of the reaction between two elements.
 */
public abstract class ReactionProduct {
  /**
   * Creates elements produced by the reaction between the given elements.
   *
   * @param reactants The input elements
   * @param cell The cell in which the reaction occurs
   */
  public abstract void react(Collection<Element> reactants, Cell cell);
}
