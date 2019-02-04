package com.p6.utils;

import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.PropertySource;

/**
 * Custom Log4j properties.
 */
public class Log4jPropertySource implements PropertySource {
  /**
   * {@inheritDoc}
   */
  @Override
  public int getPriority() {
    return -200;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void forEach(BiConsumer<String, String> action) {
    action.accept("log4j2.skipJansi", "false");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
    final StringBuilder sb = new StringBuilder("LOG4J");
    for (final CharSequence token : tokens) {
      sb.append('_');
      for (int i = 0; i < token.length(); i++) {
        sb.append(Character.toUpperCase(token.charAt(i)));
      }
    }
    return sb.toString();
  }
}
