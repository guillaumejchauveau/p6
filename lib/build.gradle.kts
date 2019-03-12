subprojects {
  group = "com.p6.lib"
  apply(plugin = "java-library")

  checkstyle {
    configFile = File("../../checkstyle.xml")
  }
}
