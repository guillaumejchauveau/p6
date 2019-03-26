package com.p6.parser.instruction;

import com.p6.parser.InvalidSyntaxException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A base class used to parse strings with the form "name(argument, ...) : ....". Those strings
 * represent a list of {@link Instruction}s. Any argument starting with the character '$' is
 * considered a reference: it is replaced by the value mapped to the remaining characters
 * following the '$'.
 *
 * @param <T> The type of the objects represented by the instructions
 */
public abstract class InstructionListParser<T extends Instruction> {
  private enum State {
    INSTRUCTION_NAME,
    INSTRUCTION_ARG,
    INSTRUCTION_POST_ARG
  }

  protected Map<String, Object> references;
  private Constructor<T> instructionConstructor;

  /**
   * Initializes a instruction list parser for a certain type of instruction.
   *
   * @param parentReferences       Inherited references
   * @param instructionConstructor The constructor used to created the corresponding instruction
   * @see Instruction
   */
  public InstructionListParser(Map<String, Object> parentReferences,
                               Constructor<T> instructionConstructor) {
    this.references = new HashMap<>(parentReferences);
    this.instructionConstructor = instructionConstructor;
  }

  /**
   * Creates a list of instructions given a string. The parsing operation is done in 3 states in
   * a loop:
   * - The instruction name: from the beginning until a character '(' or ',' is encountered,
   * corresponding to the instruction's arguments or the beginning of the next instruction.
   * - An instruction argument: if a '(' was encountered, until a ',' or ')'. The first character
   * corresponds to the next argument and the second to the end of the instruction's arguments.
   * - After the instruction's arguments: right after the precedent instruction ')' character,
   * waits for the ',' indicating the next instruction's name.
   * The arguments are optional, this is why the third state is mandatory (it is skipped if there
   * are no arguments).
   *
   * @param clause The string containing the instructions to parse
   * @return The list of instructions
   * @throws InvalidSyntaxException Thrown if the string could not be parsed
   */
  public List<T> parse(String clause) throws InvalidSyntaxException {
    List<T> instructions = new ArrayList<>();
    String buffer = "";
    T instruction = null;
    State state = State.INSTRUCTION_NAME;

    for (int i = 0; i < clause.length(); i++) {
      char currentChar = clause.charAt(i);
      boolean eol = i == clause.length() - 1;

      if (",:()".contains(Character.toString(currentChar)) || eol) {
        buffer = buffer.trim();

        switch (state) {
          case INSTRUCTION_NAME:
            try {
              instruction = this.instructionConstructor.newInstance(buffer);
            } catch (ReflectiveOperationException e) {
              throw new RuntimeException(e);
            }
            if (currentChar == '(') {
              state = State.INSTRUCTION_ARG;
            } else if (currentChar == ':' || eol) {
              instructions.add(instruction);
            }
            break;

          case INSTRUCTION_ARG:
            if (buffer.length() != 0) {
              if (buffer.charAt(0) == '$') { // Reference mapping.
                instruction.addArgument(references.get(buffer.substring(1).trim()));
              } else {
                instruction.addArgument(buffer);
              }
            }
            if (currentChar == ')') {
              instructions.add(instruction);
              state = State.INSTRUCTION_POST_ARG;
            }
            break;

          case INSTRUCTION_POST_ARG:
            if (currentChar == ':') {
              state = State.INSTRUCTION_NAME;
            }
            break;

          default:
            throw new IllegalStateException("State is illegal");
        }
        buffer = "";
      } else {
        buffer += currentChar;
      }
    }
    return instructions;
  }
}
