package com.p6.core.reaction;

import com.p6.core.solution.Element;

/**
 * An object able to test if two elements can react with each over.
 */
public abstract class ReactionCondition {
  /**
   * Test if two given elements can react.
   * @param x The first element
   * @param y The second element
   * @return The result of the test
   */
  public abstract Boolean test(Element x, Element y);
}
