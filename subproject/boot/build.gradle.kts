dependencies {
    implementation(project(":subproject:presentation"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:infrastructure"))
}

springBoot {
    mainClass.set("io.contents.CollectorApplicationKt")
}
