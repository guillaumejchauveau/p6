package com.p6.lib.common.reaction;

import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.utils.tests.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test cases for {@link DissolveCell}.
 */
@Ignore
public class DissolveCellTest extends TestCase {
  @Test
  public void test() {
    var rootCell = new Cell();
    var cell = new Cell();
    rootCell.addSubCell(cell);
    var step = new DissolveCell();
    var el = new Element<>(null) {
    };
    //product.react(el, el);
    Assert.assertTrue(cell.isDissolved());
  }
}
