package com.p6.core.reactor;

import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLPlatform;
import com.p6.core.solution.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenClReactor extends Reactor {
  private final Logger logger;

  public OpenClReactor() {
    this.logger = LogManager.getLogger();
  }

  @Override
  public void iterate(Solution solution, Integer count) {
    if (!CLPlatform.isAvailable()) {
      throw new RuntimeException("OpenCL is not available");
    }
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
