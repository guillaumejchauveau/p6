package com.p6.core.solution;

public abstract class Element<T> {
  private T value;

  public Element(T value) {
    this.assign(value);
  }

  public String toString() {
    return this.evaluate().toString();
  }

  public void assign(T value) {
    this.value = value;
  }

  public T evaluate() {
    return this.value;
  }
}
