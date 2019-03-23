package com.p6.parser.instruction;

import com.p6.lib.LibraryRegistry;
import java.util.ArrayList;
import java.util.List;

public abstract class Instruction<T> {
  public String name;
  protected List<Object> arguments;

  public Instruction(String name) {
    this.name = name;
    this.arguments = new ArrayList<>();
  }

  public List<Object> getArguments() {
    return arguments;
  }

  public void addArgument(Object argument) {
    this.arguments.add(argument);
  }

  public abstract T create(LibraryRegistry registry);
}
