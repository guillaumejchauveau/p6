java {
  sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
  implementation(project(":lib:common"))
  implementation(project(":lib"))
  implementation(project(":core"))
  implementation(project(":utils"))
  testImplementation("junit:junit:4.12")
}
