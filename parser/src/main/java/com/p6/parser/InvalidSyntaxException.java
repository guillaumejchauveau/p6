package com.p6.parser;

/**
 * Thrown when an error occurs while parsing a source with {@link StructureParser}.
 */
public class InvalidSyntaxException extends Exception {
  public final String source;
  public final Integer position;

  /**
   * Creates a new invalid syntax exception.
   *
   * @param message  Details about the error
   * @param source   The original source text
   * @param position The position of the error in the source
   */
  public InvalidSyntaxException(String message, String source, Integer position) {
    super(message);
    this.source = source;
    this.position = position;
  }

  /**
   * Creates a new invalid syntax exception without the source.
   *
   * @param message Details about the error
   */
  public InvalidSyntaxException(String message) {
    this(message, null, null);
  }
}
