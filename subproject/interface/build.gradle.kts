import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.protobuf)
}

dependencies {
    api(libs.bundles.grpc.stub)
    api(libs.bundles.protobuf)

    api(project(":subproject:interface:channel-collector-interface"))
    api(project(":subproject:interface:channel-page-collector-interface"))
}

protobuf {
    sourceSets {
        main {
            proto {
                // Include both interface modules' proto files
                srcDir("src/main/protobuf")
                srcDir("$projectDir/channel-collector-interface/protobuf")
                srcDir("$projectDir/channel-page-collector-interface/protobuf")

                // Ensure all subdirectories are included
                include("**/*.proto")
            }
        }
    }

    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protoc.get()}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.protobuf.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpc.kotlin.stub.get()}:jdk8@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
