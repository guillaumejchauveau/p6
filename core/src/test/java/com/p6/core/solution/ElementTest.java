package com.p6.core.solution;

import com.p6.utils.tests.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Element}.
 */
public class ElementTest extends TestCase {
  /**
   * Tests basic value operations for the element.
   */
  @Test
  public void valuationTest() {
    Object initialValue = 3;
    var element = new Element<>(initialValue) {
    };
    Assert.assertEquals("Evaluation should return initial value", initialValue, element.evaluate());
    Object newValue = "Hello";
    element.assign(newValue);
    Assert.assertEquals("Evaluation should return new value", newValue, element.evaluate());
  }
}
