plugins {
  `java-library`
}

dependencies {
  implementation(project(":utils"))
  implementation("org.jogamp.gluegen:gluegen-rt-main:2.3.2")
  implementation("org.jogamp.jocl:jocl-main:2.3.1")
  testImplementation("junit:junit:4.12")
}
