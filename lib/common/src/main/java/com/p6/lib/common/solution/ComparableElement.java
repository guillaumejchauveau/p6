package com.p6.lib.common.solution;

import com.p6.core.solution.Element;

public class ComparableElement<T extends Comparable<T>> extends Element<T>
    implements Comparable<ComparableElement<T>> {
  /**
   * Creates an element with a comparable pre-determined value.
   *
   * @param value The value to assign
   */
  public ComparableElement(T value) {
    super(value);
  }

  @Override
  public int compareTo(ComparableElement<T> o) {
    return this.evaluate().compareTo(o.evaluate());
  }
}
