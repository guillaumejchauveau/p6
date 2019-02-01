package com.p6.core.solution;

public abstract class Symbol<T> {
  private T value;

  public Symbol(T value) {
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
