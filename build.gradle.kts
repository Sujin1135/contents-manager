import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.dgs.codegen)
    alias(libs.plugins.graalvm)
    alias(libs.plugins.detekt)
    `java-test-fixtures`
}

group = "io.contents.collector"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {

        useJUnitPlatform()

        jvmArgs("-XX:+AllowRedefinitionToAddDeleteMethods")

        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")

            debug {
                events("standardOut", "standardError")
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }

        doFirst {
            println(">>>>> TEST JVM ARGS: ${jvmArgs?.joinToString(" ")}")
        }
    }
}

extra["netflixDgsVersion"] = "10.0.3"
extra["sentryVersion"] = "8.2.0"

subprojects {
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.jvm
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.spring
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.spring.boot
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.dependency.management
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.detekt
                .get()
                .pluginId,
    )
    apply(plugin = "java-test-fixtures")

    dependencies {
        implementation(rootProject.libs.spring.starter.webflux)
        implementation(rootProject.libs.bundles.monitoring)
        implementation(rootProject.libs.bundles.languages)
        implementation(rootProject.libs.bundles.arrow.kt)

        testImplementation(rootProject.libs.blockhound)
        testImplementation(rootProject.libs.bundles.kotest)

        testRuntimeOnly(rootProject.libs.junit.platform.launcher)
    }
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
    packageName = "io.contents.collector.codegen"
    generateClient = true
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
