import org.gradle.internal.os.OperatingSystem

plugins {
  `java-library`
}

val lwjglVersion = "3.2.1"
val lwjglNatives = when (OperatingSystem.current()) {
  OperatingSystem.LINUX -> "natives-linux"
  OperatingSystem.MAC_OS -> "natives-macos"
  OperatingSystem.WINDOWS -> "natives-windows"
  else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}
dependencies {
  implementation(project(":utils"))
  implementation("org.lwjgl", "lwjgl", lwjglVersion)
  implementation("org.lwjgl", "lwjgl-opencl", lwjglVersion)
  runtimeOnly("org.lwjgl", "lwjgl", lwjglVersion, classifier = lwjglNatives)
  testImplementation("junit:junit:4.12")
}
