val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val koin_version: String by project
val h2_version: String by project

val allureVersion = "2.26.0"
val aspectJVersion = "1.9.22"
val kotlinVersion = "1.9.23"

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

tasks.register<Test>("unit") {
    useJUnitPlatform()
    include("com/example/data/repository/**")
    include("com/example/datasource/local/dao/**")
    failFast = true
}

tasks.register<Test>("integration") {
    useJUnitPlatform()
    include("com/example/integration/**")
    shouldRunAfter("unit")
    failFast = true
}

tasks.register<Test>("e2e") {
    useJUnitPlatform()
    include("com/example/e2e/**")
    shouldRunAfter("integration")
    failFast = true
}

tasks {
    shadowJar {
        archiveBaseName.set("my-lab")
        archiveVersion.set("1.0.0")
        archiveClassifier.set("fat")
    }

    test {
        jvmArgs = listOf(
            "-javaagent:${agent.singleFile}"
        )
        maxParallelForks = Runtime.getRuntime().availableProcessors()

        dependsOn("unit", "integration", "e2e")

        useJUnitPlatform()
        systemProperty("junit.jupiter.testmethod.order.default", "random")
    }
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")

    // tests
    testImplementation("io.mockk:mockk:1.13.10")
//    testImplementation(kotlin("test"))

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-money:$exposed_version")
    implementation("org.postgresql:postgresql:42.6.0")

    implementation("com.h2database:h2:$h2_version")

    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")

    implementation("io.ktor", "ktor-server-thymeleaf-jvm", "2.3.5")
    implementation("io.ktor","ktor-server-status-pages", "2.3.5")

    implementation("io.ktor:ktor-server-swagger:$ktor_version")

    agent("org.aspectj:aspectjweaver:$aspectJVersion")

    // Tests: Use only JUnit 5 and Kotlin's test for JUnit 5
    testImplementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Используем JUnit 5 для Kotlin

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.slf4j:slf4j-simple:2.0.12")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.8.2")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
