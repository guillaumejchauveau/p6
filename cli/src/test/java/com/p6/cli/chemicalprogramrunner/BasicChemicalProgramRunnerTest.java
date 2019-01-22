package com.p6.cli.chemicalprogramrunner;

import org.junit.Test;

public class BasicChemicalProgramRunnerTest {
  @Test
  public void testExecute() {
    ChemicalProgramRunner bcpr = new BasicChemicalProgramRunner();
    bcpr.execute();
  }
}
