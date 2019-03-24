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
 * Structures can either be a string: "...\n",
 * a list of structures: "[\n
 * ...\n
 * ]",
 * or a mapping between a set of strings and structures: "{\n
 * ...:...\n
 * }".
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
   *
   */
  @FunctionalInterface
  private interface StructureCharParser {
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
   *
   * @return
   * @throws InvalidSyntaxException
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
   *
   * @return
   * @throws InvalidSyntaxException
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
   *
   * @return
   * @throws InvalidSyntaxException
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
   *
   * @param parser
   * @throws InvalidSyntaxException
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
