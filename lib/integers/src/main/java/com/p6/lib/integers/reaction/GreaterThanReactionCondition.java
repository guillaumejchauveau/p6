package com.p6.lib.integers.reaction;

import com.p6.core.reaction.ReactionCondition;
import com.p6.core.solution.Element;
import com.p6.lib.integers.IntegerElement;
import java.util.Collection;

/**
 * A condition that will make two integer elements react if the first is greater
 * than the last.
 */
public class GreaterThanReactionCondition extends ReactionCondition {
  @Override
  public Integer getInputElementsCount() {
    return 2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean test(Collection<Element> reactants) {
    Element[] elementsArray = reactants.toArray(new Element[this.getInputElementsCount()]);
    if (elementsArray[0] instanceof IntegerElement && elementsArray[1] instanceof IntegerElement) {
      IntegerElement x = (IntegerElement) elementsArray[0];
      IntegerElement y = (IntegerElement) elementsArray[1];
      return x.evaluate() > y.evaluate();
    }
    return false;
  }
}
