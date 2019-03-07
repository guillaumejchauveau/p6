package com.p6.lib.common.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import org.junit.Test;
import org.junit.Assert;

public class ReactantsReactionProductTest {
  @Test
  public void testDissolution() {
    Cell cell = new Cell();
    DissolveCellReactionProduct product = new DissolveCellReactionProduct();
    Element el = new Element<Object>(null) {

    };
    product.react(el, el, cell);
    Assert.assertTrue(cell.isDissolved());
  }
}
