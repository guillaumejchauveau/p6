package com.p6.cli.app;

import com.p6.core.run.BasicP6Runner;
import com.p6.core.run.P6Runner;
import com.p6.core.solution.Solution;
import com.p6.core.solution.SolutionConfigurator;
import java.util.ServiceLoader;

/**
 * Entry point.
 */
public class App {
  /**
   * Displays the app's message.
   *
   * @param args System args
   */
  public static void main(String[] args) {
    ServiceLoader<SolutionConfigurator> loader = ServiceLoader.load(SolutionConfigurator.class);
    Solution s = new Solution();
    for (SolutionConfigurator configurator : loader) {
      configurator.configure(s);
    }
    System.out.println(s);
    P6Runner runner = new BasicP6Runner();
    runner.iterate(s, 21000);
    System.out.println(s);
  }
}
