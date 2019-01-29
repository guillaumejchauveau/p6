plugins {
  java
  application
}

application {
  mainClassName = "com.p6.cli.app.App"
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":core"))
  runtimeOnly(project(":lib:integers"))
  runtimeOnly(project(":lib:strings"))
  testImplementation("junit:junit:4.12")
}
