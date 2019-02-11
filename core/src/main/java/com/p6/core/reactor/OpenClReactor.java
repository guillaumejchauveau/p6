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
import org.lwjgl.opencl.CL22;
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
    InfoUtil.checkClError(CL22.clGetPlatformIDs(null, pi));
    if (pi.get(0) == 0) {
      throw new RuntimeException("No OpenCL platforms found.");
    }

    PointerBuffer platforms = stack.mallocPointer(pi.get(0));
    InfoUtil.checkClError(CL22.clGetPlatformIDs(platforms, (IntBuffer) null));

    PointerBuffer ctxProps = stack.mallocPointer(3);
    ctxProps
      .put(0, CL22.CL_CONTEXT_PLATFORM)
      .put(2, 0);

    IntBuffer errcodeRet = stack.callocInt(1);
    for (int p = 0; p < platforms.capacity(); p++) {
      long platform = platforms.get(p);
      ctxProps.put(1, platform);

      logger.info("-------------------------");
      logger.info("NEW PLATFORM: [0x" + platform + "]");

      CLCapabilities platformCaps = CL.createPlatformCapabilities(platform);

      printPlatformInfo(platform, "CL_PLATFORM_PROFILE", CL22.CL_PLATFORM_PROFILE);
      printPlatformInfo(platform, "CL_PLATFORM_VERSION", CL22.CL_PLATFORM_VERSION);
      printPlatformInfo(platform, "CL_PLATFORM_NAME", CL22.CL_PLATFORM_NAME);
      printPlatformInfo(platform, "CL_PLATFORM_VENDOR", CL22.CL_PLATFORM_VENDOR);
      printPlatformInfo(platform, "CL_PLATFORM_EXTENSIONS", CL22.CL_PLATFORM_EXTENSIONS);
      if (platformCaps.cl_khr_icd) {
        printPlatformInfo(platform, "CL_PLATFORM_ICD_SUFFIX_KHR",
          KHRICD.CL_PLATFORM_ICD_SUFFIX_KHR);
      }

      InfoUtil.checkClError(CL22.clGetDeviceIDs(platform, CL22.CL_DEVICE_TYPE_ALL, null, pi));

      PointerBuffer devices = stack.mallocPointer(pi.get(0));
      InfoUtil.checkClError(CL22.clGetDeviceIDs(platform, CL22.CL_DEVICE_TYPE_ALL, devices,
        (IntBuffer) null));

      for (int d = 0; d < devices.capacity(); d++) {
        long device = devices.get(d);

        CLCapabilities caps = CL.createDeviceCapabilities(device, platformCaps);

        logger.info("\t** NEW DEVICE: [0x" + device + "]");

        logger.info("\tCL_DEVICE_TYPE = " + InfoUtil.getDeviceInfoLong(device,
          CL22.CL_DEVICE_TYPE));
        logger.info("\tCL_DEVICE_VENDOR_ID = " + InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_VENDOR_ID));
        logger.info("\tCL_DEVICE_MAX_COMPUTE_UNITS = " + InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_MAX_COMPUTE_UNITS));
        logger.info("\tCL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = " + InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS));
        logger.info("\tCL_DEVICE_MAX_WORK_GROUP_SIZE = " + InfoUtil.getDeviceInfoPointer(device,
          CL22.CL_DEVICE_MAX_WORK_GROUP_SIZE));
        logger.info("\tCL_DEVICE_MAX_CLOCK_FREQUENCY = " + InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_MAX_CLOCK_FREQUENCY));
        logger.info("\tCL_DEVICE_ADDRESS_BITS = " + InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_ADDRESS_BITS));
        logger.info("\tCL_DEVICE_AVAILABLE = " + (InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_AVAILABLE) != 0));
        logger.info("\tCL_DEVICE_COMPILER_AVAILABLE = " + (InfoUtil.getDeviceInfoInt(device,
          CL22.CL_DEVICE_COMPILER_AVAILABLE) != 0));

        printDeviceInfo(device, "CL_DEVICE_NAME", CL22.CL_DEVICE_NAME);
        printDeviceInfo(device, "CL_DEVICE_VENDOR", CL22.CL_DEVICE_VENDOR);
        printDeviceInfo(device, "CL_DRIVER_VERSION", CL22.CL_DRIVER_VERSION);
        printDeviceInfo(device, "CL_DEVICE_PROFILE", CL22.CL_DEVICE_PROFILE);
        printDeviceInfo(device, "CL_DEVICE_VERSION", CL22.CL_DEVICE_VERSION);
        printDeviceInfo(device, "CL_DEVICE_EXTENSIONS", CL22.CL_DEVICE_EXTENSIONS);
        if (caps.OpenCL11) {
          printDeviceInfo(device, "CL_DEVICE_OPENCL_C_VERSION", CL22.CL_DEVICE_OPENCL_C_VERSION);
        }

        CLContextCallback contextCb;
        long context = CL22.clCreateContext(ctxProps, device, contextCb =
          CLContextCallback.create((errinfo, privateInfo, cb, userData) -> {
            logger.error("[LWJGL] cl_context_callback");
            logger.error("\tInfo: " + MemoryUtil.memUTF8(errinfo));
          }), MemoryUtil.NULL, errcodeRet);
        InfoUtil.checkClError(errcodeRet);

        long buffer = CL22.clCreateBuffer(context, CL22.CL_MEM_READ_ONLY, 128, errcodeRet);
        InfoUtil.checkClError(errcodeRet);

        CLMemObjectDestructorCallback bufferCb1 = null;
        CLMemObjectDestructorCallback bufferCb2 = null;

        long subbuffer = MemoryUtil.NULL;

        CLMemObjectDestructorCallback subbufferCb = null;

        int errcode;

        CountDownLatch destructorLatch;

        if (caps.OpenCL11) {
          destructorLatch = new CountDownLatch(3);

          errcode = CL22.clSetMemObjectDestructorCallback(buffer, bufferCb1 =
            CLMemObjectDestructorCallback.create((memobj, userData) -> {
              logger.info("\t\tBuffer destructed (1): " + memobj);
              destructorLatch.countDown();
            }), MemoryUtil.NULL);
          InfoUtil.checkClError(errcode);

          errcode = CL22.clSetMemObjectDestructorCallback(buffer, bufferCb2 =
            CLMemObjectDestructorCallback.create((memobj, userData) -> {
              logger.info("\t\tBuffer destructed (2): " + memobj);
              destructorLatch.countDown();
            }), MemoryUtil.NULL);
          InfoUtil.checkClError(errcode);

          try (CLBufferRegion bufferRegion = CLBufferRegion.malloc()) {
            bufferRegion.origin(0);
            bufferRegion.size(64);

            subbuffer = CL22.nclCreateSubBuffer(buffer,
              CL22.CL_MEM_READ_ONLY,
              CL22.CL_BUFFER_CREATE_TYPE_REGION,
              bufferRegion.address(),
              MemoryUtil.memAddress(errcodeRet));
            InfoUtil.checkClError(errcodeRet);
          }

          errcode = CL22.clSetMemObjectDestructorCallback(subbuffer, subbufferCb =
            CLMemObjectDestructorCallback.create((memobj, userData) -> {
              logger.info("\t\tSub Buffer destructed: " + memobj);
              destructorLatch.countDown();
            }), MemoryUtil.NULL);
          InfoUtil.checkClError(errcode);
        } else {
          destructorLatch = null;
        }

        long execCaps = InfoUtil.getDeviceInfoLong(device, CL22.CL_DEVICE_EXECUTION_CAPABILITIES);
        if ((execCaps & CL22.CL_EXEC_NATIVE_KERNEL) == CL22.CL_EXEC_NATIVE_KERNEL) {
          logger.info("\t\t-TRYING TO EXEC NATIVE KERNEL-");
          long queue = CL22.clCreateCommandQueue(context, device, MemoryUtil.NULL, errcodeRet);

          PointerBuffer ev = BufferUtils.createPointerBuffer(1);

          ByteBuffer kernelArgs = BufferUtils.createByteBuffer(4);
          kernelArgs.putInt(0, 1337);

          CLNativeKernel kernel;
          errcode = CL22.clEnqueueNativeKernel(queue, kernel = CLNativeKernel.create(
            args -> logger.info("\t\tKERNEL EXEC argument: " + MemoryUtil.memByteBuffer(args, 4).getInt(0) + ", should be 1337")
          ), kernelArgs, null, null, null, ev);
          InfoUtil.checkClError(errcode);

          long e = ev.get(0);

          CountDownLatch latch = new CountDownLatch(1);

          CLEventCallback eventCb;
          errcode = CL22.clSetEventCallback(e, CL22.CL_COMPLETE, eventCb =
            CLEventCallback.create((event,
                                    eventCommandExecStatus, userData) -> {
              logger.info("\t\tEvent callback status: " + eventCommandExecStatus);
              latch.countDown();
            }), MemoryUtil.NULL);
          InfoUtil.checkClError(errcode);

          try {
            boolean expired = !latch.await(500, TimeUnit.MILLISECONDS);
            if (expired) {
              logger.info("\t\tKERNEL EXEC FAILED!");
            }
          } catch (InterruptedException exc) {
            exc.printStackTrace();
          }
          eventCb.free();

          errcode = CL22.clReleaseEvent(e);
          InfoUtil.checkClError(errcode);
          kernel.free();

          kernelArgs = BufferUtils.createByteBuffer(Pointer.POINTER_SIZE * 2);

          kernel = CLNativeKernel.create(args -> {
          });

          long time = System.nanoTime();
          int repeat = 1000;
          for (int i = 0; i < repeat; i++) {
            CL22.clEnqueueNativeKernel(queue, kernel, kernelArgs, null, null, null, null);
          }
          CL22.clFinish(queue);
          time = System.nanoTime() - time;

          System.out.printf("\n\t\tEMPTY NATIVE KERNEL AVG EXEC TIME: %.4fus\n",
            (double) time / (repeat * 1000));

          errcode = CL22.clReleaseCommandQueue(queue);
          InfoUtil.checkClError(errcode);
          kernel.free();
        }

        if (subbuffer != MemoryUtil.NULL) {
          errcode = CL22.clReleaseMemObject(subbuffer);
          InfoUtil.checkClError(errcode);
        }

        errcode = CL22.clReleaseMemObject(buffer);
        InfoUtil.checkClError(errcode);

        if (destructorLatch != null) {
          // mem object destructor callbacks are called asynchronously on Nvidia

          try {
            destructorLatch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          subbufferCb.free();

          bufferCb2.free();
          bufferCb1.free();
        }

        errcode = CL22.clReleaseContext(context);
        InfoUtil.checkClError(errcode);

        contextCb.free();
      }
    }
  }

  private void printPlatformInfo(long platform, String paramName, int param) {
    logger.info("\t" + paramName + " = " + InfoUtil.getPlatformInfoStringUtf8(platform,
      param));
  }

  private void printDeviceInfo(long device, String paramName, int param) {
    logger.info("\t" + paramName + " = " + InfoUtil.getDeviceInfoStringUtf8(device, param));
  }
}
