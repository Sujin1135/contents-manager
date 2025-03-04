dependencies {
    implementation(project(":subproject:infrastructure"))
    implementation(project(":subproject:domain"))

    testImplementation(rootProject.libs.bundles.test.setup)
    testImplementation(rootProject.libs.bundles.test.db)
}
