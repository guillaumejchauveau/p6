plugins {
  `java-library`
}

subprojects {
  group = "com.p6.lib"
  apply(plugin = "java-library")

  dependencies {
    implementation(project(":core"))
    implementation(project(":utils"))
    testImplementation("junit:junit:4.12")
  }

  checkstyle {
    configFile = File("../../checkstyle.xml")
  }
}
