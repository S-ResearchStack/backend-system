/**
 * To define Versions
 */
object Versions {
    const val SPRING_BOOT = "2.6.4"
    const val GRADLE_PLUGIN = "7.4"
    const val KOTLIN = "1.6.10"

    const val DEPENDENCY_MANAGEMENT = "1.0.11.RELEASE"
    const val R2DBC = "2.6.3"
    const val JDBC_POSTGRESQL = "42.3.2"
    const val R2DBC_POSTGRESQL = "0.8.11.RELEASE"
    const val TRINO_JDBC = "381"
    const val KTLINT = "10.2.1"
    const val DETEKT = "1.19.0"
    const val JACKSON = "2.13.1"
    const val REACTOR_TEST = "3.4.15"
    const val FLYWAY = "8.5.2"
    const val MOCKK = "1.12.3"
    const val LOGBACK = "1.2.11"
    const val FIREBASE_ADMIN = "8.2.0"

    const val KOTLIN_REACTOR_EXTENSION = "1.1.5"
    const val KOTLIN_REFLECT = "1.6.10"
    const val KOTLIN_COROUTINES_REACTOR = "1.6.0"
    const val KOTLIN_LOGGING = "2.1.20"
}

/**
 * To define Plugins
 */
object Plugins {
    val SPRING_BOOT = Plugin("org.springframework.boot", Versions.SPRING_BOOT)
    val DEPENDENCY_MANAGEMENT = Plugin("io.spring.dependency-management", Versions.DEPENDENCY_MANAGEMENT)
    val GRADLE_KTLINT = Plugin("org.jlleitschuh.gradle.ktlint", Versions.KTLINT)
    val ARTURBOSCH_DETEKT = Plugin("io.gitlab.arturbosch.detekt", Versions.DETEKT)
}

/**
 * To define libraries
 */
object Libs {
    const val SPRING_BOOT_STARTER_WEB =
        "org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_WEBFLUX =
        "org.springframework.boot:spring-boot-starter-webflux:${Versions.SPRING_BOOT}"
    const val JACKSON_MODULE_KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON}"
    const val REACTOR_KOTLIN_EXTENSION =
        "io.projectreactor.kotlin:reactor-kotlin-extensions:${Versions.KOTLIN_REACTOR_EXTENSION}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_REFLECT}"
    const val KOTLINX_COROUTINES_REACTOR =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.KOTLIN_COROUTINES_REACTOR}"
    const val DATA_R2DBC = "org.springframework.boot:spring-boot-starter-data-r2dbc:${Versions.R2DBC}"
    const val R2DBC_POSTGRESQL = "io.r2dbc:r2dbc-postgresql:${Versions.R2DBC_POSTGRESQL}"
    const val JDBC_POSTGRESQL = "org.postgresql:postgresql:${Versions.JDBC_POSTGRESQL}"
    const val TRINO_JDBC = "io.trino:trino-jdbc:${Versions.TRINO_JDBC}"
    const val FLYWAY_CORE = "org.flywaydb:flyway-core:${Versions.FLYWAY}"
    const val SPRING_BOOT_CONFIGURATION_PROCESSOR =
        "org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_SECURITY =
        "org.springframework.boot:spring-boot-starter-security:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_OAUTH2_CLIENT =
        "org.springframework.boot:spring-boot-starter-oauth2-client:${Versions.SPRING_BOOT}"
    const val SPRING_BOOT_STARTER_AOP =
        "org.springframework.boot:spring-boot-starter-aop:${Versions.SPRING_BOOT}"
    const val FIREBASE_ADMIN = "com.google.firebase:firebase-admin:${Versions.FIREBASE_ADMIN}"
    const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    const val REACTOR_TEST = "io.projectreactor:reactor-test:${Versions.REACTOR_TEST}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val KOTLIN_LOGGING = "io.github.microutils:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING}"
    const val LOGBACK = "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
}

/**
 * To define JVM config
 */
object Jvms {
    const val SOURCE_COMPATIBILITY = "17"
    const val TARGET_COMPATIBILITY = "17"
    const val JVM_TARGET = "17"
}

data class Plugin(val name: String, val version: String)
