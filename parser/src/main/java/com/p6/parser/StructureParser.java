package com.p6.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An object capable of parsing structures in a string.
 * <br>
 * Structures can either be a string: "...\n",
 * <br>
 * a list of structures: "[\n<br>
 * ...\n<br>
 * ]",
 * <br>
 * or a mapping between a set of strings and structures: "{\n<br>
 * ...:...\n<br>
 * }".
 * <br>
 * The syntax is in fact very close to JSON.
 */
public class StructureParser {
  private static final char WHITESPACE = ' ';
  private static final String LINE_TERMINATORS = "\r\n";
  private static final char LIST_START = '[';
  private static final char LIST_STOP = ']';
  private static final char MAP_START = '{';
  private static final char MAP_STOP = '}';
  private static final char COMMENT = '#';
  private static final char MAP_KEY_STOP = ':';

  private String source;
  private Integer position;
  private Stack<List<Object>> inProgressListsStack;
  private Stack<Map<String, Object>> inProgressMapsStack;

  private Logger logger;

  /**
   * An object that determines the structure corresponding to a source.
   * <br>
   * The method {@link StructureCharParser#parse(Character, Boolean)} is called for each relevant
   * character: multiple line termination characters and comments are skipped.
   *
   * @see StructureParser#parseSource(StructureCharParser)
   */
  @FunctionalInterface
  private interface StructureCharParser {
    /**
     * Called for each relevant character.
     *
     * @param currentChar The character
     * @param eol         Indicates if the character is the last of the line
     * @return An indicator for {@link StructureParser#parseSource(StructureCharParser)}: true if
     *     the structure is complete (the iteration stops); false otherwise.
     * @throws InvalidSyntaxException The implementation may throw an invalid syntax exception
     */
    Boolean parse(Character currentChar, Boolean eol) throws InvalidSyntaxException;
  }

  /**
   * Initializes a new structure parser with the source to parse.
   *
   * @param source The source to parse
   */
  public StructureParser(String source) {
    this.source = source.trim() + "\n"; // Ensures that EOF is on an empty line.
    this.position = 0;
    this.inProgressListsStack = new Stack<>();
    this.inProgressListsStack.push(null);
    this.inProgressMapsStack = new Stack<>();
    this.inProgressMapsStack.push(null);
    this.logger = LogManager.getLogger();
  }

  /**
   * Crawls the source until an entire structure is parsed.
   *
   * @return The structure found
   * @throws InvalidSyntaxException Thrown if the syntax of the source is invalid
   */
  public Object parseNextStructure() throws InvalidSyntaxException {
    AtomicReference<Object> structure = new AtomicReference<>();
    structure.set(null);

    this.parseSource((Character currentChar, Boolean eol) -> {
      if (currentChar != WHITESPACE && !eol) {
        switch (currentChar) {
          case LIST_START:
            structure.set(this.parseNextList());
            break;
          case MAP_START:
            structure.set(this.parseNextMap());
            break;
          default:
            structure.set(this.parseNextString());
            break;
        }
        this.position--; // Compensates last position increment by parseSource.
        return true;
      }
      return false;
    });
    return structure.get();
  }

  /**
   * Crawls the source to extract a string structure. The string is supposed to end at EOL.
   *
   * @return The extracted string
   */
  private String parseNextString() throws InvalidSyntaxException {
    StringBuilder builder = new StringBuilder();
    this.parseSource((Character currentChar, Boolean eol) -> {
      if (eol) {
        return true;
      }
      builder.append(currentChar);
      return false;
    });
    return builder.toString().trim();
  }

  /**
   * Crawls the source to extract an entire list. The position at start must be on the list's start
   * character (or before it if there are whitespaces).
   * <br>
   * Then, each structures of the list must be on their own line. They will be parsed with
   * {@link StructureParser#parseNextStructure}.
   * <br>
   * The list's end character is also expected on a new line.
   *
   * @return The extracted list
   */
  private List parseNextList() throws InvalidSyntaxException {
    List<Object> list = new ArrayList<>();

    this.parseSource((Character currentChar, Boolean eol) -> {
      if (currentChar == LIST_START && this.inProgressListsStack.peek() != list) {
        this.inProgressListsStack.push(list);
        return false;
      }
      if (currentChar == LIST_STOP) {
        if (this.inProgressListsStack.peek() != list) {
          throw new IllegalStateException("Unexpected unterminated list");
        }
        return true;
      }
      if (currentChar == MAP_STOP) {
        throw new InvalidSyntaxException("Unexpected map termination", this.source, this.position);
      }
      if (currentChar != WHITESPACE && !eol) {
        if (this.inProgressListsStack.peek() == null) {
          throw new InvalidSyntaxException("List start expected", this.source, this.position);
        }
        list.add(this.parseNextStructure());
        this.position--; // Compensates last position increment by parseSource.
      }
      return false;
    });

    return this.inProgressListsStack.pop();
  }

  /**
   * Crawls the source to extract an entire map. The position at start must be on the map's start
   * character (or before it if there are whitespaces).
   * <br>
   * The method expects to find a string followed with the character ':', indicating the key for
   * the value. The beginning of the structure for the value must be on the same line as it's key.
   * <br>
   * The structure is parsed using {@link StructureParser#parseNextStructure}. The next value's
   * key must be on a new line.
   *
   * @return The extracted map
   */
  private Map<String, Object> parseNextMap() throws InvalidSyntaxException {
    Map<String, Object> map = new HashMap<>();
    AtomicReference<StringBuilder> key = new AtomicReference<>();

    this.parseSource((Character currentChar, Boolean eol) -> {
      if (currentChar == MAP_START && this.inProgressMapsStack.peek() != map) {
        this.inProgressMapsStack.push(map);
        return false;
      }
      if (currentChar == MAP_STOP) {
        if (this.inProgressMapsStack.peek() != map) {
          throw new IllegalStateException("Unexpected unterminated map");
        }
        return true;
      }
      if (currentChar == LIST_STOP) {
        logger.debug(this.position);
        throw new InvalidSyntaxException("Unexpected list termination", this.source, this.position);
      }

      if (currentChar == MAP_KEY_STOP) {
        if (key.get() == null) {
          throw new InvalidSyntaxException("Map key expected", this.source, this.position);
        }
        this.position++; // Skips MAP_KEY_STOP.
        map.put(key.toString().trim(), this.parseNextStructure());
        this.position--; // Goes back to the previous entry's EOL.
        key.set(null);
        return false;
      }

      if (currentChar != WHITESPACE && !eol && key.get() == null) {
        key.set(new StringBuilder());
      }

      if (key.get() != null) {
        if (eol) {
          throw new InvalidSyntaxException("Map value expected", this.source, this.position);
        } else {
          key.get().append(currentChar);
        }
      }
      return false;
    });

    return this.inProgressMapsStack.pop();
  }

  /**
   * Iterates over the source's character by updating the current position and detects any line
   * termination sequences or comments. The {@link StructureCharParser} given as argument is
   * called only if the current position should not be skipped.
   *
   * @param parser A function that use the current position's character to actually parse the source
   */
  private void parseSource(StructureCharParser parser) throws InvalidSyntaxException {
    boolean skipLine = false;
    boolean eol = false;
    boolean stop = false;

    for (; this.position < this.source.length() && !stop; this.position++) {
      char currentChar = this.source.charAt(this.position);

      if (LINE_TERMINATORS.contains(Character.toString(currentChar))) {
        skipLine = false;
        if (eol) {
          continue;
        }
        eol = true;
      } else if (eol) {
        eol = false;
      } else if (skipLine) {
        continue;
      }

      if (currentChar == COMMENT) {
        skipLine = true;
        continue;
      }

      if (this.position == this.source.length() - 1) {
        eol = true;
      }

      stop = parser.parse(currentChar, eol || skipLine);
    }
    if (!stop) {
      this.logger.warn("Last structure parser did not stop gracefully: EOF was reached");
    }
  }
}
