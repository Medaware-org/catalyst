plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.openapi.generator") version "6.6.0"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "org.medaware"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
    generatorName = "kotlin-spring"
    inputSpec = "$rootDir/openapi/spec.yaml"
    outputDir = "$buildDir/generated"
    apiPackage = "org.medaware.catalyst.api"
    modelPackage = "org.medaware.catalyst.dto"
    invokerPackage = "org.medaware.catalyst"
    openApiGenerate.configOptions.put("useSpringBoot3", "true")
    openApiGenerate.configOptions.put("useTags", "true")
    openApiGenerate.configOptions.put("interfaceOnly", "true")
}

sourceSets {
    main {
        kotlin.srcDirs("$rootDir/build/generated/src/main/kotlin")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}