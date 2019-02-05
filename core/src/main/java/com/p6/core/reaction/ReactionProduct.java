package com.p6.core.reaction;

import com.p6.core.solution.Element;
import java.util.Collection;

/**
 * An object that determines the product of the reaction between two elements.
 */
public abstract class ReactionProduct {
  /**
   * Creates elements produced by the reaction between two elements.
   *
   * @param x The first element
   * @param y The second element
   * @return A collection of all the created elements.
   */
  public abstract Collection<Element> getProducts(Element x, Element y);
}
