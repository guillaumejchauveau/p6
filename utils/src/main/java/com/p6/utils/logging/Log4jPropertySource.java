package com.p6.utils.logging;

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
    var sb = new StringBuilder("LOG4J");
    for (var token : tokens) {
      sb.append('_');
      for (var i = 0; i < token.length(); i++) {
        sb.append(Character.toUpperCase(token.charAt(i)));
      }
    }
    return sb.toString();
  }
}
