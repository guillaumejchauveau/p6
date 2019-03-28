package com.p6.utils.logging.plugins;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

/**
 * A Log4J filter that puts it's current thread asleep to slow down the log.
 */
@Plugin(name = "SleepFilter", category = Core.CATEGORY_NAME, elementType = Filter.ELEMENT_TYPE)
public class SleepFilter extends AbstractFilter {
  public static final Marker MARKER = MarkerManager.getMarker("SLEEP");
  private Long sleepTime;

  private SleepFilter(Long sleepTime, Result onMatch, Result onMismatch) {
    super(onMatch, onMismatch);
    this.sleepTime = sleepTime;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Filter.Result filter(LogEvent event) {
    if (event.getMarker() == SleepFilter.MARKER) {
      try {
        Thread.sleep(this.sleepTime);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    return super.filter(event);
  }

  @PluginFactory
  public static SleepFilter createFilter(@PluginAttribute("time") Long sleepTime,
                                         @PluginAttribute(AbstractFilterBuilder.ATTR_ON_MATCH)
                                           Result match,
                                         @PluginAttribute(AbstractFilterBuilder.ATTR_ON_MISMATCH)
                                           Result mismatch) {
    return new SleepFilter(sleepTime, match, mismatch);
  }
}
