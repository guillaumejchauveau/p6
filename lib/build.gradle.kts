plugins {
  `java-library`
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":core"))
  testImplementation("junit:junit:4.12")
}

subprojects {
  group = "com.p6.lib"
  apply(plugin = "java-library")

  checkstyle {
    configFile = File("../../checkstyle.xml")
  }
}
