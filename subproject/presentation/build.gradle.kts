dependencies {
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:application"))

    implementation(rootProject.libs.dgs.spring.graphql.starter)
}
