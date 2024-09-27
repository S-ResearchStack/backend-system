import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.proto
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("com.google.protobuf") version "0.8.18"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.3.2"
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("jacoco")
    id("idea")
    kotlin("plugin.spring") version "1.9.0"
    kotlin("kapt") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"

    application
}

group = "researchstack.backend"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.armeria.bom)
    implementation(libs.armeria.grpc)
    implementation(libs.armeria.kotlin)
    implementation(libs.armeria.springBoot.webflux)
    implementation(libs.aws.s3TransferManager)
    implementation(libs.aws.sts)
    implementation(libs.casbin.springBoot)
    implementation(libs.flyway.core)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.protobuf)
    implementation(libs.guava)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.javax.annotationApi)
    implementation(libs.json)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutinesReactor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor)
    implementation(platform(libs.netty.bom))
    implementation(libs.opencsv)
    implementation(libs.playtika.feignReactor)
    implementation(libs.protobuf.java.util)
    implementation(libs.reactor.kotlin.extensions)
    implementation(libs.springBoot.aop)
    implementation(libs.springBoot.jdbc)
    implementation(libs.springBoot.mail)
    implementation(libs.springBoot.mongodbReactive)
    implementation(libs.springBoot.oauth2Client)
    implementation(libs.springBoot.quartz)
    implementation(libs.springBoot.redis)
    implementation(libs.springBoot.webflux)
    implementation(libs.springKafka)
    implementation(libs.trino.jdbc)
    implementation(libs.trino.parser)

    // Logging
    runtimeOnly(libs.logback.classic)
    runtimeOnly(libs.slf4j.log4j)
    implementation(libs.zip4j)

    testImplementation(libs.blockhound)
    testImplementation(libs.embeddedRedis)
    testImplementation(libs.junit.jupiterEngine)
    testImplementation(libs.junit.jupiterParams)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutinesTest)
    testImplementation(libs.kotlinx.coroutinesDebug)
    testImplementation(libs.marcinziolo.wiremock)
    testImplementation(libs.mockk)
    testImplementation(libs.reactor.test)
    testImplementation(libs.springBoot.test)
    testImplementation(libs.springKafkaTest)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.wiremock)
}

sourceSets {
    main {
        proto {
            srcDir("../proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

application {
    mainClass.set("researchstack.backend.HealthResearchBackendAppKt")
}

jacoco {
    toolVersion = "0.8.9"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

// Exclude protobuf generated files & the main spring boot application class
val jacocoExcludeFiles = listOf("**/generated/**", "**/HealthResearchBackendAppKt.class", "**/backend/grpc/**")

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    extensions.configure(JacocoTaskExtension::class) {
        excludes = jacocoExcludeFiles
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
        xml.outputLocation.set(File("$buildDir/reports/jacoco/report.xml"))
        html.outputLocation.set(File("$buildDir/reports/jacoco/report.html"))
    }
    classDirectories.setFrom(
        classDirectories.files.map {
            fileTree(it).matching {
                exclude(jacocoExcludeFiles)
            }
        }
    )
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.6500".toBigDecimal()
            }
        }
    }
    classDirectories.setFrom(
        classDirectories.files.map {
            fileTree(it).matching {
                exclude(jacocoExcludeFiles)
            }
        }
    )
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test>().all {
    if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_13)) {
        jvmArgs("-XX:+AllowRedefinitionToAddDeleteMethods")
    }
}
