package com.p6.lib;

/**
 * An object capable of creating an object given arguments.
 *
 * @param <T> The type of the created object
 */
@FunctionalInterface
public interface InitArgsParser<T> {
  /**
   * Creates a new instance of the object.
   *
   * @param args The arguments for the constructor
   * @return The created object
   */
  T parseArgs(Object[] args);
}
