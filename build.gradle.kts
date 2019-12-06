import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version de.novatec.graphqlclient.build.BuildConfig.springBootVersion
    id("io.spring.dependency-management") version de.novatec.graphqlclient.build.BuildConfig.springDependencyManagement
    kotlin("jvm") version de.novatec.graphqlclient.build.BuildConfig.kotlinVersion
    kotlin("plugin.spring") version "1.3.50"
    id("com.apollographql.android") version de.novatec.graphqlclient.build.BuildConfig.apolloVersion
}

group = "de.novatec"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

sourceSets {
    main {
        allSource.srcDirs.add(file("build/generated/source/apollo/classes/main"))
    }
}

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.apollographql.apollo:apollo-runtime:${de.novatec.graphqlclient.build.BuildConfig.apolloVersion}")
    implementation("com.apollographql.apollo:apollo-coroutines-support:${de.novatec.graphqlclient.build.BuildConfig.apolloVersion}")
    implementation("io.github.microutils:kotlin-logging:1.7.7")

    compileOnly("org.jetbrains:annotations:13.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")

    testCompileOnly("org.jetbrains:annotations:13.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${de.novatec.graphqlclient.build.BuildConfig.springCloudVersion}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

apollo {
    outputPackageName.set("de.novatec.graphqlclient.queries")
    generateKotlinModels.set(true)
    customTypeMapping.put("URI", "java.net.URI")
}
