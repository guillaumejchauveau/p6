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
  implementation(project(":lib:common"))
  implementation(project(":lib:integers"))
  testImplementation("junit:junit:4.12")
}
