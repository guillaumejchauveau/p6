package com.p6.core.solution;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.reaction.ReactionProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cell {
  /**
   * The program's rules. A rule or reaction is defined with a condition that
   * must be fulfilled and the products of the reaction.
   */
  protected Map<ReactionCondition, ReactionProduct> rules;
  /**
   * The solution's elements.
   */
  protected List<Element> elements;

  public Cell() {
    this.rules = new HashMap<>();
    this.elements = new ArrayList<>();
  }
  /**
   * Adds a rule to the solution given a reaction condition and a reaction
   * product.
   *
   * @param reactionCondition The condition for the reaction
   * @param reactionProduct   The product of the reaction
   */
  public void createRule(ReactionCondition reactionCondition, ReactionProduct reactionProduct) {
    this.rules.put(reactionCondition, reactionProduct);
  }

  /**
   * Adds an element to the solution.
   * @param element The element to add
   */
  public void addElement(Element element) {
    this.elements.add(element);
  }
}
