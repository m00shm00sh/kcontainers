import org.jetbrains.dokka.gradle.*

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlinx.atomicfu") version "0.26.1"
    id("org.jetbrains.dokka") version "1.9.20"
    `maven-publish`
}

group = "com.moshy.containers"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("reflect"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.test {
    useJUnitPlatform()
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
}


java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
