plugins {
  java
  application
  jacoco
  checkstyle
  maven
}

application {
  mainClassName = "app.App"
}

dependencies {
  testCompile("junit:junit:4.12")
  testCompile("org.mockito:mockito-core:2.+")
}

checkstyle {
  configFile = File("../checkstyle.xml")
}

tasks.withType<Checkstyle>().configureEach {
  reports {
    xml.isEnabled = true
    html.isEnabled = true
  }
}

tasks.jacocoTestReport {
  reports {
    xml.isEnabled = true
  }
}

tasks.check {
  dependsOn(tasks.jacocoTestReport)
}

task("writePom") {
  doLast {
    maven.pom {
    }.writeTo("pom.xml")
  }
}
