package com.p6.parser.instruction;

import com.p6.core.genesis.ElementGenerator;
import com.p6.lib.LibraryRegistry;

/**
 *
 */
public class ElementGeneratorInstruction extends Instruction<ElementGenerator> {
  /**
   *
   * @param name
   */
  public ElementGeneratorInstruction(String name) {
    super(name);
  }

  /**
   *
   * @param registry
   * @return
   */
  @Override
  public ElementGenerator create(LibraryRegistry registry) {
    return registry.createElementGenerator(this.name, this.arguments.toArray());
  }
}
