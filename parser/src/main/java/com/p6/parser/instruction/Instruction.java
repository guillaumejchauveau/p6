package com.p6.parser.instruction;

import com.p6.lib.LibraryRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class used to represent the data of strings with the form "name(argument, ...)".
 *
 * @param <T> The type of the object corresponding to the instruction
 */
public abstract class Instruction<T> {
  /**
   * The name of the instruction.
   */
  public final String name;
  protected List<Object> arguments;

  /**
   * Initializes a new instruction with a name.
   *
   * @param name The name of the instruction
   */
  public Instruction(String name) {
    this.name = name;
    this.arguments = new ArrayList<>();
  }

  /**
   * @return The list of arguments for the instruction
   */
  public List<Object> getArguments() {
    return arguments;
  }

  /**
   * Adds an argument for the instruction.
   *
   * @param argument The argument
   */
  public void addArgument(Object argument) {
    this.arguments.add(argument);
  }

  /**
   * Creates the object corresponding to the instruction.
   *
   * @param registry The library registry used to create the object
   * @return The created object
   */
  public abstract T create(LibraryRegistry registry);
}
