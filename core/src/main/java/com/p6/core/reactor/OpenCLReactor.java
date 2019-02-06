package com.p6.core.reactor;

import com.p6.core.solution.Solution;
import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_queue_properties;

public class OpenCLReactor extends Reactor {
  private static String programSource =
    "__kernel void " +
      "sampleKernel(__global const float *a," +
      "             __global const float *b," +
      "             __global float *c)" +
      "{" +
      "    int gid = get_global_id(0);" +
      "    c[gid] = a[gid] * b[gid];" +
      "}";

  @Override
  public void iterate(Solution solution, Integer count) {
    int n = 200000000;
    float[] srcArrayA = new float[n];
    float[] srcArrayB = new float[n];
    float[] dstArray = new float[n];
    for (int i = 0; i < n; i++) {
      srcArrayA[i] = i;
      srcArrayB[i] = i;
    }
    Pointer srcA = Pointer.to(srcArrayA);
    Pointer srcB = Pointer.to(srcArrayB);
    Pointer dst = Pointer.to(dstArray);

    // The platform, device type and device number
    // that will be used
    final int platformIndex = 0;
    final long deviceType = CL.CL_DEVICE_TYPE_ALL;
    final int deviceIndex = 0;

    // Enable exceptions and subsequently omit error checks in this sample
    CL.setExceptionsEnabled(true);

    // Obtain the number of platforms
    int[] numPlatformsArray = new int[1];
    CL.clGetPlatformIDs(0, null, numPlatformsArray);
    int numPlatforms = numPlatformsArray[0];

    // Obtain a platform ID
    cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
    CL.clGetPlatformIDs(platforms.length, platforms, null);
    var platform = platforms[platformIndex];

    // Initialize the context properties
    var contextProperties = new cl_context_properties();
    contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);

    // Obtain the number of devices for the platform
    int[] numDevicesArray = new int[1];
    CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
    int numDevices = numDevicesArray[0];

    // Obtain a device ID
    cl_device_id[] devices = new cl_device_id[numDevices];
    CL.clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
    var device = devices[deviceIndex];

    // Create a context for the selected device
    var context = CL.clCreateContext(
      contextProperties, 1, new cl_device_id[]{device},
      null, null, null);
    var queueProperties = new cl_queue_properties();
    // Create a command-queue for the selected device
    var commandQueue =
      CL.clCreateCommandQueueWithProperties(context, device, queueProperties, null);

    // Allocate the memory objects for the input- and output data
    cl_mem[] memObjects = new cl_mem[3];
    memObjects[0] = CL.clCreateBuffer(context,
      CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
      Sizeof.cl_float * n, srcA, null);
    memObjects[1] = CL.clCreateBuffer(context,
      CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
      Sizeof.cl_float * n, srcB, null);
    memObjects[2] = CL.clCreateBuffer(context,
      CL.CL_MEM_READ_WRITE,
      Sizeof.cl_float * n, null, null);

    // Create the program from the source code
    var program = CL.clCreateProgramWithSource(context,
      1, new String[]{OpenCLReactor.programSource}, null, null);

    // Build the program
    CL.clBuildProgram(program, 0, null, null, null, null);

    // Create the kernel
    var kernel = CL.clCreateKernel(program, "sampleKernel", null);

    // Set the arguments for the kernel
    CL.clSetKernelArg(kernel, 0,
      Sizeof.cl_mem, Pointer.to(memObjects[0]));
    CL.clSetKernelArg(kernel, 1,
      Sizeof.cl_mem, Pointer.to(memObjects[1]));
    CL.clSetKernelArg(kernel, 2,
      Sizeof.cl_mem, Pointer.to(memObjects[2]));

    // Set the work-item dimensions
    long[] global_work_size = new long[]{n};
    long[] local_work_size = new long[]{1};

    // Execute the kernel
    CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
      global_work_size, local_work_size, 0, null, null);

    // Read the output data
    CL.clEnqueueReadBuffer(commandQueue, memObjects[2], CL.CL_TRUE, 0,
      n * Sizeof.cl_float, dst, 0, null, null);

    // Release kernel, program, and memory objects
    CL.clReleaseMemObject(memObjects[0]);
    CL.clReleaseMemObject(memObjects[1]);
    CL.clReleaseMemObject(memObjects[2]);
    CL.clReleaseKernel(kernel);
    CL.clReleaseProgram(program);
    CL.clReleaseCommandQueue(commandQueue);
    CL.clReleaseContext(context);

    // Verify the result
    boolean passed = true;
    final float epsilon = 1e-7f;
    for (int i = 0; i < n; i++) {
      float x = dstArray[i];
      float y = srcArrayA[i] * srcArrayB[i];
      boolean epsilonEqual = Math.abs(x - y) <= epsilon * Math.abs(x);
      if (!epsilonEqual) {
        passed = false;
        break;
      }
    }
    System.out.println("Test " + (passed ? "PASSED" : "FAILED"));
  }
}
