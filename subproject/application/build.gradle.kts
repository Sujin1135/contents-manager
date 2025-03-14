dependencies {
    implementation(project(":subproject:infrastructure"))
    implementation(project(":subproject:domain"))

    testImplementation(project(":subproject:infrastructure"))
    testImplementation(project(":subproject:interface"))
    testImplementation(testFixtures(project(":subproject:infrastructure")))

    testImplementation(rootProject.libs.bundles.test.setup)
    testImplementation(rootProject.libs.bundles.test.db)
}
