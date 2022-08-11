plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.named("jar") {
    enabled = false
}

dependencies {
    implementation(Libs.SPRING_BOOT_STARTER_WEBFLUX)
    implementation(Libs.SPRING_BOOT_STARTER_SECURITY)
    implementation(Libs.SPRING_BOOT_STARTER_OAUTH2_CLIENT)
    implementation(Libs.SPRING_BOOT_STARTER_AOP)
    implementation(Libs.JACKSON_MODULE_KOTLIN)
    implementation(Libs.REACTOR_KOTLIN_EXTENSION)
    implementation(Libs.KOTLIN_REFLECT)
    implementation(Libs.KOTLINX_COROUTINES_REACTOR)
    implementation(Libs.DATA_R2DBC)
    implementation(Libs.FLYWAY_CORE)
    implementation(Libs.R2DBC_POSTGRESQL)
    implementation(Libs.KOTLIN_LOGGING)
    implementation(Libs.LOGBACK)
    implementation(Libs.FIREBASE_ADMIN)
    runtimeOnly(Libs.JDBC_POSTGRESQL)
    annotationProcessor(Libs.SPRING_BOOT_CONFIGURATION_PROCESSOR)
    testImplementation(Libs.SPRING_BOOT_STARTER_TEST)
    testImplementation(Libs.REACTOR_TEST)
    testImplementation(Libs.MOCKK)
}
