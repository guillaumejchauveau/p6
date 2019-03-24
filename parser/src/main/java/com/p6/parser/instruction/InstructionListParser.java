package com.p6.parser.instruction;

import com.p6.parser.InvalidSyntaxException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */
public class InstructionListParser<T extends Instruction> {
  private enum State {
    INSTRUCTION_NAME,
    INSTRUCTION_ARG,
    INSTRUCTION_POST_ARG
  }

  protected Map<String, Object> references;
  private Constructor<T> instructionConstructor;

  /**
   *
   * @param parentReferences
   * @param instructionConstructor
   */
  public InstructionListParser(Map<String, Object> parentReferences,
                               Constructor<T> instructionConstructor) {
    this.references = new HashMap<>(parentReferences);
    this.instructionConstructor = instructionConstructor;
  }

  /**
   *
   * @param clause
   * @return
   * @throws InvalidSyntaxException
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
              if (buffer.charAt(0) == '$') {
                instruction.addArgument(references.get(buffer.substring(1)));
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
