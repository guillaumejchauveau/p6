package com.p6.core.reactor;

import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLPlatform;
import com.p6.core.solution.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenCLReactor extends Reactor {
  private final Logger logger;

  public OpenCLReactor() {
    this.logger = LogManager.getLogger();
  }

  @Override
  public void iterate(Solution solution, Integer count) {
    CLPlatform.initialize();
    CLPlatform[] platforms = CLPlatform.listCLPlatforms();
    for (CLPlatform platform : platforms) {
      this.logger.debug(platform);
      for (CLDevice device : platform.listCLDevices()) {
        this.logger.debug(device.getName() + ": " + device.getMaxComputeUnits() + " units @ " + device.getMaxClockFrequency() + " MHz");
      }
    }
  }
}
