package com.p6.parser;

import com.p6.core.genesis.ElementGenerator;
import com.p6.lib.LibraryRegistry;
import com.p6.parser.instruction.ElementGeneratorInstruction;
import com.p6.parser.instruction.InstructionListParser;
import java.util.List;
import java.util.Map;

/**
 * Parses a list of instructions to create an element generator. As an element generator is
 * defined with only one instruction, the string to parse is supposed to have the form "name
 * (argument...)". The list of instruction parsed by {@link InstructionListParser#parse} should
 * contain only one element.
 */
public class ElementGeneratorParser extends InstructionListParser<ElementGeneratorInstruction> {
  /**
   * Initializes the instruction list parser for {@link ElementGeneratorInstruction}s.
   *
   * @param parentReferences Inherited references
   * @throws ReflectiveOperationException Thrown most likely in the case of a programmatic error
   */
  public ElementGeneratorParser(Map<String, Object> parentReferences)
      throws ReflectiveOperationException {
    super(parentReferences, ElementGeneratorInstruction.class.getConstructor(String.class));
  }

  /**
   * Creates the element generator defined by the input string using a {@link LibraryRegistry}.
   *
   * @param clause   The string to parse
   * @param registry The registry to use
   * @return The created element generator
   * @throws InvalidSyntaxException Thrown of the string cannot be parsed
   */
  public ElementGenerator create(String clause, LibraryRegistry registry)
      throws InvalidSyntaxException {
    List<ElementGeneratorInstruction> instructions = this.parse(clause);
    if (instructions.size() == 0) {
      throw new InvalidSyntaxException("Element generator instruction expected", clause, 0);
    }
    return instructions.get(0).create(registry);
  }
}
