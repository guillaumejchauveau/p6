plugins {
  java
  application
  jacoco
}

application {
  mainClassName = "app.App"
}

dependencies {
  testCompile("junit:junit:4.12")
}

tasks.jacocoTestReport {
  reports {
    xml.isEnabled = true
  }
}

tasks.check {
  dependsOn(tasks.jacocoTestReport)
}
