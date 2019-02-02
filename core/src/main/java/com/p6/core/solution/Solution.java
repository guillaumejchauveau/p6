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

public class Solution {
  private Map<ReactionCondition, ReactionProduct> rules;
  private List<Element> elements;
  private Random random;

  public Solution() {
    this.rules = new HashMap<>();
    this.elements = new ArrayList<>();
    this.random = new Random();
  }

  public String toString() {
    return this.rules.size() + " rules, " + this.elements.size() + " elements";
  }

  public void createRule(ReactionCondition reactionCondition, ReactionProduct reactionProduct) {
    this.rules.put(reactionCondition, reactionProduct);
  }

  public Set<ReactionCondition> getConditions() {
    return this.rules.keySet();
  }

  public void applyRule(ReactionCondition reactionCondition, Element x, Element y) {
    ReactionProduct rule = this.rules.get(reactionCondition);
    this.addAllElements(rule.getProducts(x, y));
  }

  public void addElement(Element element) {
    this.elements.add(element);
  }

  public void addAllElements(Collection<Element> elements) {
    this.elements.addAll(elements);
  }

  public Integer getElementsCount() {
    return this.elements.size();
  }

  public Element chooseElement() {
    int elementIndex = this.random.nextInt(this.getElementsCount());
    Element element = this.elements.get(elementIndex);
    this.elements.remove(elementIndex);
    return element;
  }
}
