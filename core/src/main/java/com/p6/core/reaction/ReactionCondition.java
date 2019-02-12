package com.p6.core.reaction;

import com.p6.core.solution.Element;
import java.util.Collection;

/**
 * An object able to test if two elements can react with each over.
 */
public abstract class ReactionCondition {
  /**
   * Test if the given elements can react.
   * @param reactants The elements for the reaction
   * @return The result of the test
   */
  public abstract Boolean test(Collection<Element> reactants);

  /**
   * A getter to the number of required elements for the reaction.
   *
   * @return The number of input elements
   */
  public abstract Integer getInputElementsCount();
}
