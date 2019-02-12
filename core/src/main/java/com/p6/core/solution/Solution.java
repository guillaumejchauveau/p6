package com.p6.core.solution;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.reaction.ReactionProduct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * An object representing a P6 program with it's rules and elements.
 */
public class Solution extends Cell {
  /**
   * A random number generator used to select elements of the solution.
   */
  private Random random;

  /**
   * Creates a new solution.
   */
  public Solution() {
    super();
    this.random = new Random();
  }

  public String toString() {
    return this.rules.size() + " rules, " + this.elements.size() + " elements";
  }

  /**
   * A getter used for testing if a reaction can be applied.
   * @return A set of the registered reaction conditions.
   */
  public Set<ReactionCondition> getConditions() {
    return this.rules.keySet();
  }

  /**
   * Adds a reaction's product given it's corresponding condition.
   * @param reactionCondition The condition of the reaction
   * @param x The first element for the reaction
   * @param y The second element
   */
  public void applyRule(ReactionCondition reactionCondition, Element x, Element y) {
    ReactionProduct product = this.rules.get(reactionCondition);
    //product.react(, this);
  }

  /**
   * Adds a collection of elements to the solution.
   * @param elements The elements to add
   */
  public void addAllElements(Collection<Element> elements) {
    this.elements.addAll(elements);
  }

  /**
   * The number of elements in the solution.
   * @return The number of element in the solution
   */
  public Integer getElementsCount() {
    return this.elements.size();
  }

  /**
   * Selects a random element from the solution. The element is removed from the
   * solution so that it cannot be re-selected.
   * @return The chosen element
   */
  public Element chooseElement() {
    int elementIndex = this.random.nextInt(this.getElementsCount());
    Element element = this.elements.get(elementIndex);
    this.elements.remove(elementIndex);
    return element;
  }
}
