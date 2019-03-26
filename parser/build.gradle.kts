plugins {
  `java-library`
}

dependencies {
  implementation(project(":utils"))
  implementation(project(":core"))
  implementation(project(":lib"))
  testImplementation("junit:junit:4.12")
}
