package com.p6.core.genesis;

import com.p6.core.solution.Element;
import java.util.List;

/**
 *
 */
@FunctionalInterface
public interface ElementGenerator {
  /**
   *
   */
  List<Element> generate();
}
