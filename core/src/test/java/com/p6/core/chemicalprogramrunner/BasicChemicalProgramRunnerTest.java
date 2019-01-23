package com.p6.core.chemicalprogramrunner;

import org.junit.Test;

public class BasicChemicalProgramRunnerTest {
  @Test
  public void testExecute() {
    ChemicalProgramRunner bcpr = new BasicChemicalProgramRunner();
    bcpr.execute();
  }
}
