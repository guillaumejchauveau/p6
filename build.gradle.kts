allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  version = "1.0"
}

plugins {
  id("com.gradle.build-scan") version "2.1"
}

buildScan {
  setTermsOfServiceUrl("https://gradle.com/terms-of-service")
  setTermsOfServiceAgree("yes")
}

