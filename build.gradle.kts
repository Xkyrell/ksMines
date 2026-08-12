import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.0.0"
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    kotlin("plugin.lombok") version "2.2.21"
    kotlin("kapt") version "2.2.21"
}

group = "me.xkyrell"
version = "1.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.12.3")
    implementation("com.charleskorn.kaml:kaml:0.104.0")

    implementation("com.google.dagger:dagger:2.59.2")
    kapt("com.google.dagger:dagger-compiler:2.59.2")

    // ksTemporal (deprecated)
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<ShadowJar> {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")

        exclude("META-INF/**")
        exclude("colors.bin")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

kotlin {
    jvmToolchain(21)
}

bukkit {
    name = rootProject.name
    author = "Xkyrell"
    version = project.version.toString()
    main = "me.xkyrell.ksmines.MinesPlugin"
    apiVersion = "1.21"
    softDepend = listOf("PlaceholderAPI")
}