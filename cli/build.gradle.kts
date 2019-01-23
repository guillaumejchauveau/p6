plugins {
  java
  application
  jacoco
  checkstyle
  maven
}

application {
  mainClassName = "com.p6.cli.app.App"
}

dependencies {
  testCompile("junit:junit:4.12")
  implementation(project(":core"))
  runtimeOnly(project(":lib:integers"))
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
