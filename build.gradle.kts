import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "6.6.0"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "org.medaware"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

fun copyOpenApiSpec(): Unit =
    run {
        Files.copy(
            Path.of("openapi/spec.yaml"),
            Path.of("$rootDir/src/main/resources/static/spec.yaml"),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

openApiGenerate {
    copyOpenApiSpec()

    generatorName = "kotlin-spring"
    inputSpec = "openapi/spec.yaml"
    invokerPackage = "org.medaware.catalyst"
    modelPackage = "org.medaware.catalyst.model"
    apiPackage = "org.medaware.catalyst.openapi.controllers"
    groupId = "org.medaware.catalyst"
    id = "medaware-catalyst"

    configOptions.put("useSpringBoot3", "true")
    configOptions.put("interfaceOnly", "true")
    configOptions.put("useTags", "true")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.apache.lucene:lucene-core:9.10.0")
    implementation("org.apache.lucene:lucene-queryparser:9.10.0")
}

sourceSets {
    main {
        java {
            kotlin.srcDirs("$rootDir/build/generate-resources/main")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.compileKotlin {
    doFirst {
        copyOpenApiSpec()
    }
    dependsOn("openApiGenerate")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
