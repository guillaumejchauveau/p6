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
  implementation(project(":lib:common"))
  implementation(project(":lib:integers"))
  implementation(project(":parser"))
  testImplementation("junit:junit:4.12")
}
