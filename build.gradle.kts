allprojects {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.gradle.build-scan") version "2.1"
  id("io.freefair.aggregate-javadoc") version "3.1.4"
  jacoco
  checkstyle
  maven
}

tasks.clean {
  subprojects.forEach {
    val cleanTask = it.tasks.findByName("clean")
    if (cleanTask != null) {
      dependsOn(cleanTask)
    }
  }
}

tasks.register<Copy>("dist") {
  dependsOn("aggregateJavadoc", ":cli:installDist")
  from("build/docs/aggregateJavadoc") {
    into("docs")
  }
  from("cli/build/install/cli/lib") {
    into("cli/lib")
  }
  into("dist")
  finalizedBy("clean")
}

tasks.register<Delete>("cleanDist") {
  dependsOn("clean")
  delete("dist/docs", "dist/cli", fileTree("dist/demos").matching {
    include("**/*.class")
  })
}

subprojects {
  group = "com.p6"
  version = "1.0"

  apply(plugin = "jacoco")
  apply(plugin = "checkstyle")
  apply(plugin = "maven")

  tasks.withType<JacocoReport>().configureEach {
    reports {
      xml.isEnabled = true
    }
  }

  tasks.check {
    dependsOn(tasks.withType<JacocoReport>())
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

