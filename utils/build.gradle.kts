plugins {
  `java-library`
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  runtime("org.fusesource.jansi:jansi:1.17.1")
  api("org.apache.logging.log4j:log4j-core:2.11.1")
  testImplementation("junit:junit:4.12")
}
