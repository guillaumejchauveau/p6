plugins {
    java
  application
}

application {
  mainClassName = "com.p6.gui.app.App"
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":core"))
  testImplementation("junit:junit:4.12")
}

