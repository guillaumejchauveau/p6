package demo;

import com.p6.core.reaction.ReactionPipeline;
import com.p6.core.reactor.ReactorCoordinator;
import com.p6.core.solution.Cell;
import com.p6.core.solution.Element;
import com.p6.lib.LibraryRegistry;
import com.p6.utils.logging.LoggingHelper;
import demo.CharElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;


public class LibraryAPI {
  public static void main(String[] args) throws InterruptedException {
    LoggingHelper.configureLoggingFramework(Level.DEBUG);

    var registry = new LibraryRegistry();
  }
}
