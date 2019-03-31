java {
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  implementation(project(":core"))
  implementation(project(":lib"))
  implementation(project(":utils"))
  testImplementation("junit:junit:4.12")
}
