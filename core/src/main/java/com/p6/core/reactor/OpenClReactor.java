package com.p6.core.reactor;

import com.p6.core.solution.Solution;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import static org.lwjgl.opencl.CL22.*;
import org.lwjgl.opencl.CLBufferRegion;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLContextCallback;
import org.lwjgl.opencl.CLEventCallback;
import org.lwjgl.opencl.CLMemObjectDestructorCallback;
import org.lwjgl.opencl.CLNativeKernel;
import org.lwjgl.opencl.KHRICD;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

public class OpenClReactor extends Reactor {
  private final Logger logger;

  public OpenClReactor() {
    this.logger = LogManager.getLogger();
  }

  @Override
  public void iterate(Solution solution, Integer count) {
    MemoryStack stack = MemoryStack.stackPush();
    IntBuffer pi = stack.mallocInt(1);
    InfoUtil.checkCLError(clGetPlatformIDs(null, pi));
    if (pi.get(0) == 0) {
      throw new RuntimeException("No OpenCL platforms found.");
    }

    PointerBuffer platforms = stack.mallocPointer(pi.get(0));
    InfoUtil.checkCLError(clGetPlatformIDs(platforms, (IntBuffer) null));

    PointerBuffer ctxProps = stack.mallocPointer(3);
    ctxProps
      .put(0, CL_CONTEXT_PLATFORM)
      .put(2, 0);

    IntBuffer errcode_ret = stack.callocInt(1);
    for (int p = 0; p < platforms.capacity(); p++) {
      long platform = platforms.get(p);
      ctxProps.put(1, platform);

      System.out.println("\n-------------------------");
      System.out.printf("NEW PLATFORM: [0x%X]\n", platform);

      CLCapabilities platformCaps = CL.createPlatformCapabilities(platform);

      printPlatformInfo(platform, "CL_PLATFORM_PROFILE", CL_PLATFORM_PROFILE);
      printPlatformInfo(platform, "CL_PLATFORM_VERSION", CL_PLATFORM_VERSION);
      printPlatformInfo(platform, "CL_PLATFORM_NAME", CL_PLATFORM_NAME);
      printPlatformInfo(platform, "CL_PLATFORM_VENDOR", CL_PLATFORM_VENDOR);
      printPlatformInfo(platform, "CL_PLATFORM_EXTENSIONS", CL_PLATFORM_EXTENSIONS);
      if (platformCaps.cl_khr_icd) {
        printPlatformInfo(platform, "CL_PLATFORM_ICD_SUFFIX_KHR",
          KHRICD.CL_PLATFORM_ICD_SUFFIX_KHR);
      }
      System.out.println("");

      InfoUtil.checkCLError(clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, null, pi));

      PointerBuffer devices = stack.mallocPointer(pi.get(0));
      InfoUtil.checkCLError(clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, devices,
        (IntBuffer) null));

      for (int d = 0; d < devices.capacity(); d++) {
        long device = devices.get(d);

        CLCapabilities caps = CL.createDeviceCapabilities(device, platformCaps);

        System.out.printf("\n\t** NEW DEVICE: [0x%X]\n", device);

        System.out.println("\tCL_DEVICE_TYPE = " + InfoUtil.getDeviceInfoLong(device,
          CL_DEVICE_TYPE));
        System.out.println("\tCL_DEVICE_VENDOR_ID = " + InfoUtil.getDeviceInfoInt(device,
          CL_DEVICE_VENDOR_ID));
        System.out.println("\tCL_DEVICE_MAX_COMPUTE_UNITS = " + InfoUtil.getDeviceInfoInt(device,
          CL_DEVICE_MAX_COMPUTE_UNITS));
        System.out
          .println("\tCL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = " + InfoUtil.getDeviceInfoInt(device,
            CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS));
        System.out.println("\tCL_DEVICE_MAX_WORK_GROUP_SIZE = " + InfoUtil.getDeviceInfoPointer(device, CL_DEVICE_MAX_WORK_GROUP_SIZE));
        System.out.println("\tCL_DEVICE_MAX_CLOCK_FREQUENCY = " + InfoUtil.getDeviceInfoInt(device, CL_DEVICE_MAX_CLOCK_FREQUENCY));
        System.out.println("\tCL_DEVICE_ADDRESS_BITS = " + InfoUtil.getDeviceInfoInt(device,
          CL_DEVICE_ADDRESS_BITS));
        System.out.println("\tCL_DEVICE_AVAILABLE = " + (InfoUtil.getDeviceInfoInt(device,
          CL_DEVICE_AVAILABLE) != 0));
        System.out.println("\tCL_DEVICE_COMPILER_AVAILABLE = " + (InfoUtil.getDeviceInfoInt(device, CL_DEVICE_COMPILER_AVAILABLE) != 0));

        printDeviceInfo(device, "CL_DEVICE_NAME", CL_DEVICE_NAME);
        printDeviceInfo(device, "CL_DEVICE_VENDOR", CL_DEVICE_VENDOR);
        printDeviceInfo(device, "CL_DRIVER_VERSION", CL_DRIVER_VERSION);
        printDeviceInfo(device, "CL_DEVICE_PROFILE", CL_DEVICE_PROFILE);
        printDeviceInfo(device, "CL_DEVICE_VERSION", CL_DEVICE_VERSION);
        printDeviceInfo(device, "CL_DEVICE_EXTENSIONS", CL_DEVICE_EXTENSIONS);
        if (caps.OpenCL11) {
          printDeviceInfo(device, "CL_DEVICE_OPENCL_C_VERSION", CL_DEVICE_OPENCL_C_VERSION);
        }

        CLContextCallback contextCB;
        long context = clCreateContext(ctxProps, device, contextCB =
          CLContextCallback.create((errinfo, private_info, cb, user_data) -> {
          System.err.println("[LWJGL] cl_context_callback");
          System.err.println("\tInfo: " + MemoryUtil.memUTF8(errinfo));
        }), MemoryUtil.NULL, errcode_ret);
        InfoUtil.checkCLError(errcode_ret);

        long buffer = clCreateBuffer(context, CL_MEM_READ_ONLY, 128, errcode_ret);
        InfoUtil.checkCLError(errcode_ret);

        CLMemObjectDestructorCallback bufferCB1 = null;
        CLMemObjectDestructorCallback bufferCB2 = null;

        long subbuffer = MemoryUtil.NULL;

        CLMemObjectDestructorCallback subbufferCB = null;

        int errcode;

        CountDownLatch destructorLatch;

        if (caps.OpenCL11) {
          destructorLatch = new CountDownLatch(3);

          errcode = clSetMemObjectDestructorCallback(buffer, bufferCB1 =
            CLMemObjectDestructorCallback.create((memobj, user_data) -> {
            System.out.println("\t\tBuffer destructed (1): " + memobj);
            destructorLatch.countDown();
          }), MemoryUtil.NULL);
          InfoUtil.checkCLError(errcode);

          errcode = clSetMemObjectDestructorCallback(buffer, bufferCB2 =
            CLMemObjectDestructorCallback.create((memobj, user_data) -> {
            System.out.println("\t\tBuffer destructed (2): " + memobj);
            destructorLatch.countDown();
          }), MemoryUtil.NULL);
          InfoUtil.checkCLError(errcode);

          try (CLBufferRegion buffer_region = CLBufferRegion.malloc()) {
            buffer_region.origin(0);
            buffer_region.size(64);

            subbuffer = nclCreateSubBuffer(buffer,
              CL_MEM_READ_ONLY,
              CL_BUFFER_CREATE_TYPE_REGION,
              buffer_region.address(),
              MemoryUtil.memAddress(errcode_ret));
            InfoUtil.checkCLError(errcode_ret);
          }

          errcode = clSetMemObjectDestructorCallback(subbuffer, subbufferCB =
            CLMemObjectDestructorCallback.create((memobj, user_data) -> {
            System.out.println("\t\tSub Buffer destructed: " + memobj);
            destructorLatch.countDown();
          }), MemoryUtil.NULL);
          InfoUtil.checkCLError(errcode);
        } else {
          destructorLatch = null;
        }

        long exec_caps = InfoUtil.getDeviceInfoLong(device, CL_DEVICE_EXECUTION_CAPABILITIES);
        if ((exec_caps & CL_EXEC_NATIVE_KERNEL) == CL_EXEC_NATIVE_KERNEL) {
          System.out.println("\t\t-TRYING TO EXEC NATIVE KERNEL-");
          long queue = clCreateCommandQueue(context, device, MemoryUtil.NULL, errcode_ret);

          PointerBuffer ev = BufferUtils.createPointerBuffer(1);

          ByteBuffer kernelArgs = BufferUtils.createByteBuffer(4);
          kernelArgs.putInt(0, 1337);

          CLNativeKernel kernel;
          errcode = clEnqueueNativeKernel(queue, kernel = CLNativeKernel.create(
            args -> System.out.println("\t\tKERNEL EXEC argument: " + MemoryUtil.memByteBuffer(args, 4).getInt(0) + ", should be 1337")
          ), kernelArgs, null, null, null, ev);
          InfoUtil.checkCLError(errcode);

          long e = ev.get(0);

          CountDownLatch latch = new CountDownLatch(1);

          CLEventCallback eventCB;
          errcode = clSetEventCallback(e, CL_COMPLETE, eventCB = CLEventCallback.create((event,
                                                                                         event_command_exec_status, user_data) -> {
            System.out.println("\t\tEvent callback status: " + event_command_exec_status);
            latch.countDown();
          }), MemoryUtil.NULL);
          InfoUtil.checkCLError(errcode);

          try {
            boolean expired = !latch.await(500, TimeUnit.MILLISECONDS);
            if (expired) {
              System.out.println("\t\tKERNEL EXEC FAILED!");
            }
          } catch (InterruptedException exc) {
            exc.printStackTrace();
          }
          eventCB.free();

          errcode = clReleaseEvent(e);
          InfoUtil.checkCLError(errcode);
          kernel.free();

          kernelArgs = BufferUtils.createByteBuffer(Pointer.POINTER_SIZE * 2);

          kernel = CLNativeKernel.create(args -> {
          });

          long time = System.nanoTime();
          int REPEAT = 1000;
          for (int i = 0; i < REPEAT; i++) {
            clEnqueueNativeKernel(queue, kernel, kernelArgs, null, null, null, null);
          }
          clFinish(queue);
          time = System.nanoTime() - time;

          System.out.printf("\n\t\tEMPTY NATIVE KERNEL AVG EXEC TIME: %.4fus\n",
            (double) time / (REPEAT * 1000));

          errcode = clReleaseCommandQueue(queue);
          InfoUtil.checkCLError(errcode);
          kernel.free();
        }

        System.out.println();

        if (subbuffer != MemoryUtil.NULL) {
          errcode = clReleaseMemObject(subbuffer);
          InfoUtil.checkCLError(errcode);
        }

        errcode = clReleaseMemObject(buffer);
        InfoUtil.checkCLError(errcode);

        if (destructorLatch != null) {
          // mem object destructor callbacks are called asynchronously on Nvidia

          try {
            destructorLatch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          subbufferCB.free();

          bufferCB2.free();
          bufferCB1.free();
        }

        errcode = clReleaseContext(context);
        InfoUtil.checkCLError(errcode);

        contextCB.free();
      }
    }
  }

  private static void printPlatformInfo(long platform, String param_name, int param) {
    System.out.println("\t" + param_name + " = " + InfoUtil.getPlatformInfoStringUTF8(platform, param));
  }

  private static void printDeviceInfo(long device, String param_name, int param) {
    System.out.println("\t" + param_name + " = " + InfoUtil.getDeviceInfoStringUTF8(device, param));
  }
}
