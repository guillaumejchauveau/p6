package app;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {
  @Test
  public void testAppMessage() {
    App app = new App();
    Assert.assertNotNull("App should have a message", app.message());
  }
}
