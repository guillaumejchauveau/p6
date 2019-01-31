package com.p6.cli.app;

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
    for (SolutionConfigurator configurator : loader) {
      System.out.println(configurator);
    }
  }
}
