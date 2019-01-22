package com.p6.cli.app;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {
  @Test
  public void testAppEntryPoint() {
    String[] args = null;
    App.main(args);
  }

  @Test
  public void testAppMessage() {
    App app = new App();
    Assert.assertNotNull("App should have a message", app.message());
  }
}
