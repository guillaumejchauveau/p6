package com.p6.core.solution;

/**
 * A unit of data in a cell.
 *
 * @param <T> The type of the represented data
 */
public abstract class Element<T> {
  /**
   * The represented data.
   */
  private T value;

  /**
   * Creates an element with a pre-determined value.
   *
   * @param value The value to assign
   */
  public Element(T value) {
    this.assign(value);
  }

  public String toString() {
    return this.evaluate().toString();
  }

  /**
   * Assigns a new value to the element.
   *
   * @param value The value to assign
   */
  public void assign(T value) {
    this.value = value;
  }

  /**
   * Evaluates the element's value.
   *
   * @return The element's value
   */
  public T evaluate() {
    return this.value;
  }

  /**
   * Describes an element's side in pipeline.
   */
  public enum Side {
    /**
     * The first element of the pipeline.
     */
    LEFT,
    /**
     * The second or last element of the pipeline.
     */
    RIGHT
  }
}
