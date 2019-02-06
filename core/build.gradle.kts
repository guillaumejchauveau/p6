plugins {
  `java-library`
}

dependencies {
  implementation(project(":utils"))
  implementation("org.jocl:jocl:2.0.1")
  testImplementation("junit:junit:4.12")
}
