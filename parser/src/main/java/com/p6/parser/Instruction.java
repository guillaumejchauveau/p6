package com.p6.parser;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

  public final String name;
  private List<Object> arguments;

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


}
