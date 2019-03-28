plugins {
  java
  application
}

application {
  mainClassName = "com.p6.cli.App"
}

tasks.jar {
  manifest {
    attributes(
      "Main-Class" to application.mainClassName
    )
  }
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":core"))
  implementation(project(":lib"))
  runtime(project(":lib:common"))
  runtime(project(":lib:integers"))
  implementation(project(":parser"))
  implementation("info.picocli:picocli:3.9.5")
  testImplementation("junit:junit:4.12")
}
