package com.p6.parser.instruction;

import com.p6.core.genesis.ElementGenerator;
import com.p6.lib.LibraryRegistry;

/**
 * An {@link Instruction} representing an {@link ElementGenerator}.
 */
public class ElementGeneratorInstruction extends Instruction<ElementGenerator> {
  /**
   * {@inheritDoc}
   */
  public ElementGeneratorInstruction(String name) {
    super(name);
  }

  /**
   * Creates the element generator corresponding to the instruction.
   *
   * @param registry The registry used to create the element generator
   * @return The element generator created
   */
  @Override
  public ElementGenerator create(LibraryRegistry registry) {
    return registry.createElementGenerator(this.name, this.arguments.toArray());
  }
}
