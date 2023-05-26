import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "me.stanislav"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    jcenter {
        content {
            includeModule("org.jetbrains.kotlinx", "kotlinx-collections-immutable-jvm")
        }
    }
}

dependencies {
    implementation ("org.jetbrains.kotlinx:kotlinx-collections-immutable-jvm:0.3.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3-native-mt"){
        version {
            strictly("1.6.3-native-mt")
        }
    }

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}