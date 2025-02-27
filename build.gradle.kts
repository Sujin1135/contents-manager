plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "7.0.3"
    id("org.graalvm.buildtools.native") version "0.10.5"
}

group = "io.contents"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["netflixDgsVersion"] = "10.0.3"
extra["sentryVersion"] = "8.2.0"

dependencies {
    implementation(libs.bundles.languages)
    implementation(libs.spring.starter.webflux)
    implementation(libs.bundles.monitoring)
    implementation(libs.bundles.db.setup)
    runtimeOnly(libs.bundles.postgres)
    implementation(libs.dgs.spring.graphql.starter)
    testImplementation(libs.bundles.test.setup)
    testImplementation(libs.bundles.test.db)
    testRuntimeOnly(libs.junit.platform.launcher)
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
        mavenBom("io.sentry:sentry-bom:${property("sentryVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.generateJava {
    schemaPaths.add("$projectDir/src/main/resources/graphql-client")
    packageName = "io.contents.codegen"
    generateClient = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
