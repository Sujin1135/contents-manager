dependencies {
    runtimeOnly(rootProject.libs.bundles.postgres)
    implementation(rootProject.libs.bundles.db.setup)
    implementation(rootProject.libs.bundles.grpc.stub)
    implementation(rootProject.libs.grpc.client.spring)

    implementation(project(":subproject:interface"))
    implementation(project(":subproject:domain"))

    testFixturesImplementation(project(":subproject:interface"))
}
