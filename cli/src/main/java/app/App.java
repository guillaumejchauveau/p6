package app;

/**
 * Entry point
 */
public class App {
  /**
   * Displays the app's message.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    App app = new App();
    System.out.println(app.message());
  }

  /**
   * @return The app's message
   */
  public String message() {
    return "Hello world";
  }
}
