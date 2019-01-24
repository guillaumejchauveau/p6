allprojects {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.gradle.build-scan") version "2.1"
  jacoco
  checkstyle
  maven
}

subprojects {
  group = "com.p6"
  version = "1.0"

  apply(plugin = "jacoco")
  apply(plugin = "checkstyle")
  apply(plugin = "maven")

  checkstyle {
    configFile = File("../checkstyle.xml")
  }

  tasks.withType<Checkstyle>().configureEach {
    reports {
      xml.isEnabled = true
      html.isEnabled = true
    }
  }

  task("writePom") {
    doLast {
      maven.pom {
      }.writeTo("pom.xml")
    }
  }
}

buildScan {
  setTermsOfServiceUrl("https://gradle.com/terms-of-service")
  setTermsOfServiceAgree("yes")
}

