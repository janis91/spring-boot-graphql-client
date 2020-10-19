import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version de.novatec.graphqlclient.build.BuildConfig.springBootVersion
    id("io.spring.dependency-management") version de.novatec.graphqlclient.build.BuildConfig.springDependencyManagement
    kotlin("jvm") version de.novatec.graphqlclient.build.BuildConfig.kotlinVersion
    kotlin("plugin.spring") version de.novatec.graphqlclient.build.BuildConfig.kotlinSpringVersion
    id("com.apollographql.apollo") version de.novatec.graphqlclient.build.BuildConfig.apolloVersion
}

group = "de.novatec"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets {
    val integration by creating {
        compileClasspath += main.get().output + test.get().output
        runtimeClasspath += main.get().output + test.get().output
    }
    val acceptance by creating {
        compileClasspath += main.get().output + test.get().output
        runtimeClasspath += main.get().output + test.get().output
    }
}

val integrationImplementation by configurations.getting {
    extendsFrom(configurations["testImplementation"])
}
val integrationRuntimeOnly by configurations.getting {
    extendsFrom(configurations["testRuntimeOnly"])
}
val integrationAnnotationProcessor by configurations.getting {
    extendsFrom(configurations["testAnnotationProcessor"])
}
val integrationCompileOnly by configurations.getting {
    extendsFrom(configurations["testCompileOnly"])
}
val acceptanceImplementation by configurations.getting {
    extendsFrom(configurations["testImplementation"])
}
val acceptanceRuntimeOnly by configurations.getting {
    extendsFrom(configurations["testRuntimeOnly"])
}
val acceptanceAnnotationProcessor by configurations.getting {
    extendsFrom(configurations["testAnnotationProcessor"])
}
val acceptanceCompileOnly by configurations.getting {
    extendsFrom(configurations["testCompileOnly"])
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/apollographql/android") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.apollographql.apollo:apollo-runtime:${de.novatec.graphqlclient.build.BuildConfig.apolloVersion}")
    implementation("com.apollographql.apollo:apollo-coroutines-support:${de.novatec.graphqlclient.build.BuildConfig.apolloVersion}")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation("com.squareup.okio:okio:2.5.0")

    compileOnly("org.jetbrains:annotations:13.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.ninja-squad:springmockk:${de.novatec.graphqlclient.build.BuildConfig.springMockkVersion}")

    testCompileOnly("org.jetbrains:annotations:13.0")

    integrationImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${de.novatec.graphqlclient.build.BuildConfig.springCloudVersion}")
    }
}

tasks.withType<Test> {
    testLogging {
        events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.STARTED)
        displayGranularity = 0
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.FULL
    }
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

apollo {
    generateKotlinModels.set(true)
    service("mesh") {
        sourceFolder.set("de/novatec/mesh")
        rootPackageName.set("de.novatec.graphqlclient.mesh")
        customTypeMapping.set(mapOf("BigDecimal" to "java.math.BigDecimal"))
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = sourceSets.getByName("integration").output.classesDirs
    classpath = sourceSets.getByName("integration").runtimeClasspath
}

tasks.register<Test>("acceptanceTest") {
    description = "Runs the acceptance tests."
    group = "verification"
    testClassesDirs = sourceSets.getByName("acceptance").output.classesDirs
    classpath = sourceSets.getByName("acceptance").runtimeClasspath
}