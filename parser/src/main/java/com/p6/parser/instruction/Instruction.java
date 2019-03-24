package com.p6.parser.instruction;

import com.p6.lib.LibraryRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 */
public abstract class Instruction<T> {
  /**
   *
   */
  public final String name;
  protected List<Object> arguments;

  /**
   *
   * @param name
   */
  public Instruction(String name) {
    this.name = name;
    this.arguments = new ArrayList<>();
  }

  /**
   *
   * @return
   */
  public List<Object> getArguments() {
    return arguments;
  }

  /**
   *
   * @param argument
   */
  public void addArgument(Object argument) {
    this.arguments.add(argument);
  }

  /**
   *
   * @param registry
   * @return
   */
  public abstract T create(LibraryRegistry registry);
}
