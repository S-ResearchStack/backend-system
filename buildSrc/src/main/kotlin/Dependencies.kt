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
    const val TRINO_JDBC = "403"
    const val TRINO_PARSER = "403"
    const val KTLINT = "10.2.1"
    const val DETEKT = "1.19.0"
    const val JACKSON = "2.13.1"
    const val GSON = "2.9.0"
    const val REACTOR_TEST = "3.4.15"
    const val FLYWAY = "8.5.2"
    const val MOCKK = "1.12.3"
    const val SPRING_MOCKK = "3.1.1"
    const val LOGBACK = "1.2.11"
    const val FIREBASE_ADMIN = "8.2.0"
    const val FEIGN_REACTOR = "3.2.5"
    const val WIRE_MOCK = "2.0.1"
    const val MAPSTRUCT = "1.5.2.Final"
    const val NETFLIX_GRAPHQL_DGS = "5.5.1"
    const val CRON_EXPRESSION = "2.3.2"
    const val ANTLR = "4.11.1"
    const val LIBRARIES_BOM = "26.10.0"
    const val GOOGLE_CLOUD_STORAGE = "2.20.1"

    const val KOTLIN_REACTOR_EXTENSION = "1.1.5"
    const val KOTLIN_REFLECT = "1.6.10"
    const val KOTLINX_COROUTINES = "1.6.0"
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
    const val SPRING_BOOT_STARTER_MAIL = "org.springframework.boot:spring-boot-starter-mail:${Versions.SPRING_BOOT}"
    const val JACKSON_MODULE_KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val REACTOR_KOTLIN_EXTENSION =
        "io.projectreactor.kotlin:reactor-kotlin-extensions:${Versions.KOTLIN_REACTOR_EXTENSION}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_REFLECT}"
    const val KOTLINX_COROUTINES_REACTOR =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.KOTLINX_COROUTINES}"
    const val DATA_R2DBC = "org.springframework.boot:spring-boot-starter-data-r2dbc:${Versions.R2DBC}"
    const val R2DBC_POSTGRESQL = "io.r2dbc:r2dbc-postgresql:${Versions.R2DBC_POSTGRESQL}"
    const val JDBC_POSTGRESQL = "org.postgresql:postgresql:${Versions.JDBC_POSTGRESQL}"
    const val TRINO_JDBC = "io.trino:trino-jdbc:${Versions.TRINO_JDBC}"
    const val TRINO_PARSER = "io.trino:trino-parser:${Versions.TRINO_PARSER}"
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
    const val KOTLINX_COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.KOTLINX_COROUTINES}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val SPRING_MOCKK = "com.ninja-squad:springmockk:${Versions.SPRING_MOCKK}"
    const val KOTLIN_LOGGING = "io.github.microutils:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING}"
    const val LOGBACK = "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
    const val ANTLR = "org.antlr:antlr4:${Versions.ANTLR}"
    const val LIBRARIES_BOM = "com.google.cloud:libraries-bom:${Versions.LIBRARIES_BOM}"
    const val GOOGLE_CLOUD_STORAGE = "com.google.cloud:google-cloud-storage:${Versions.GOOGLE_CLOUD_STORAGE}"

    const val FEIGN_REACTOR_WEBCLIENT =
        "com.playtika.reactivefeign:feign-reactor-spring-cloud-starter:${Versions.FEIGN_REACTOR}"
    const val WIRE_MOCK = "com.marcinziolo:kotlin-wiremock:${Versions.WIRE_MOCK}"
    const val MAPSTRUCT_IMPL = "org.mapstruct:mapstruct:${Versions.MAPSTRUCT}"
    const val MAPSTRUCT_ANNOTATION = "org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}"

    const val DGS_PLATFORM = "com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${Versions.NETFLIX_GRAPHQL_DGS}"
    const val DGS_SPRING_BOOT_STARTER =
        "com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:${Versions.NETFLIX_GRAPHQL_DGS}"

    const val CRON_EXPRESSION = "org.quartz-scheduler:quartz:${Versions.CRON_EXPRESSION}"
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
