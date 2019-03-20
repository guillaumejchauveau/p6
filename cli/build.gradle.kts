plugins {
  java
  application
}

application {
  mainClassName = "com.p6.cli.app.App"
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
  testImplementation("junit:junit:4.12")
}
