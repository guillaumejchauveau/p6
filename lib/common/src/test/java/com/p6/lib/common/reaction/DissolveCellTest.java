package com.p6.lib.common.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.tests.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class DissolveCellTest extends TestCase {
  @Test
  public void test() {
    Cell rootCell = new Cell();
    Cell cell = new Cell();
    rootCell.addSubCell(cell);
    DissolveCell step = new DissolveCell();
    Element el = new Element<>(null) {
    };
    //product.react(el, el);
    Assert.assertTrue(cell.isDissolved());
  }
}
