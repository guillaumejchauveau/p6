package demo;

import com.p6.lib.LibraryRegistry;
import com.p6.utils.logging.LoggingHelper;
import org.apache.logging.log4j.Level;


public class LibraryDemo {
  public static void main(String[] args) {
    LoggingHelper.configureLoggingFramework(Level.DEBUG);

    var registry = new LibraryRegistry();
  }
}
