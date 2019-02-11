/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

package com.p6.core.reactor;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL22;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * OpenCL object info utilities.
 */
public final class InfoUtil {

  private InfoUtil() {
  }

  public static String getPlatformInfoStringAscii(long clPlatformId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetPlatformInfo(clPlatformId, paramName, (ByteBuffer) null, pp));
      int bytes = (int) pp.get(0);

      ByteBuffer buffer = stack.malloc(bytes);
      checkClError(CL22.clGetPlatformInfo(clPlatformId, paramName, buffer, null));

      return MemoryUtil.memASCII(buffer, bytes - 1);
    }
  }

  public static String getPlatformInfoStringUtf8(long clPlatformId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetPlatformInfo(clPlatformId, paramName, (ByteBuffer) null, pp));
      int bytes = (int) pp.get(0);

      ByteBuffer buffer = stack.malloc(bytes);
      checkClError(CL22.clGetPlatformInfo(clPlatformId, paramName, buffer, null));

      return MemoryUtil.memUTF8(buffer, bytes - 1);
    }
  }

  public static int getDeviceInfoInt(long clDeviceId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer pl = stack.mallocInt(1);
      checkClError(CL22.clGetDeviceInfo(clDeviceId, paramName, pl, null));
      return pl.get(0);
    }
  }

  public static long getDeviceInfoLong(long clDeviceId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      LongBuffer pl = stack.mallocLong(1);
      checkClError(CL22.clGetDeviceInfo(clDeviceId, paramName, pl, null));
      return pl.get(0);
    }
  }

  public static long getDeviceInfoPointer(long clDeviceId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetDeviceInfo(clDeviceId, paramName, pp, null));
      return pp.get(0);
    }
  }

  public static String getDeviceInfoStringUtf8(long clDeviceId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetDeviceInfo(clDeviceId, paramName, (ByteBuffer) null, pp));
      int bytes = (int) pp.get(0);

      ByteBuffer buffer = stack.malloc(bytes);
      checkClError(CL22.clGetDeviceInfo(clDeviceId, paramName, buffer, null));

      return MemoryUtil.memUTF8(buffer, bytes - 1);
    }
  }

  public static long getMemObjectInfoPointer(long clMem, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetMemObjectInfo(clMem, paramName, pp, null));
      return pp.get(0);
    }
  }

  public static long getMemObjectInfoInt(long clMem, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer pi = stack.mallocInt(1);
      checkClError(CL22.clGetMemObjectInfo(clMem, paramName, pi, null));
      return pi.get(0);
    }
  }

  public static int getProgramBuildInfoInt(long clProgramId, long clDeviceId, int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer pl = stack.mallocInt(1);
      checkClError(CL22.clGetProgramBuildInfo(clProgramId, clDeviceId, paramName, pl, null));
      return pl.get(0);
    }
  }

  public static String getProgramBuildInfoStringAscii(long clProgramId, long clDeviceId,
                                                      int paramName) {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      PointerBuffer pp = stack.mallocPointer(1);
      checkClError(CL22.clGetProgramBuildInfo(clProgramId, clDeviceId, paramName,
        (ByteBuffer) null, pp));
      int bytes = (int) pp.get(0);

      ByteBuffer buffer = stack.malloc(bytes);
      checkClError(CL22.clGetProgramBuildInfo(clProgramId, clDeviceId, paramName, buffer, null));

      return MemoryUtil.memASCII(buffer, bytes - 1);
    }
  }

  public static void checkClError(IntBuffer errcode) {
    checkClError(errcode.get(errcode.position()));
  }

  public static void checkClError(int errcode) {
    if (errcode != CL22.CL_SUCCESS) {
      throw new RuntimeException(String.format("OpenCL error [%d]", errcode));
    }
  }
}
