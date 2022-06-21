import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
}

group = "es.juandavidvega.ecc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.3")

    testImplementation(kotlin("test"))
}

tasks.register<Sync>("dist") {
    dependsOn("jar")
    from("${project.rootDir}/ecc-cli.tmpl") {
        rename {
            "ecc-cli"
        }
    }
    from("${project.projectDir}/build/libs/${project.name}-${project.version}.jar") {
        into("lib")
    }
    into("${project.rootDir}/dist")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("es.juandavidvega.ecc.cli.AppKt")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "es.juandavidvega.ecc.cli.AppKt"
    }

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
