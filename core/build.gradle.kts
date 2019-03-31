plugins {
  `java-library`
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  implementation(project(":utils"))
  testImplementation("junit:junit:4.12")
}
