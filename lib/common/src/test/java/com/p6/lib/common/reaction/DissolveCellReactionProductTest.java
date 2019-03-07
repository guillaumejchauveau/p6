package com.p6.lib.common.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.tests.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DissolveCellReactionProductTest extends TestCase {
  @Test
  public void testDissolution() {
    Cell cell = new Cell();
    DissolveCellReactionProduct product = new DissolveCellReactionProduct();
    Element el = new Element<>(null) {
    };
    product.react(el, el, cell);
    Assert.assertTrue(cell.isDissolved());
  }
}
