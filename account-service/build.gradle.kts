plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.named("jar") {
    enabled = false
}

dependencies {
    implementation(project(":account-role"))
    implementation(Libs.SPRING_BOOT_STARTER_WEBFLUX)
    implementation(Libs.SPRING_BOOT_STARTER_AOP)
    implementation(Libs.SPRING_BOOT_STARTER_MAIL)
    implementation(Libs.SPRING_BOOT_STARTER_OAUTH2_CLIENT)
    implementation(Libs.JACKSON_MODULE_KOTLIN)
    implementation(Libs.REACTOR_KOTLIN_EXTENSION)
    implementation(Libs.FEIGN_REACTOR_WEBCLIENT)

    testImplementation(Libs.SPRING_BOOT_STARTER_TEST) {
        exclude(module = "mockito-core")
    }
    testImplementation(Libs.REACTOR_TEST)
    testImplementation(Libs.MOCKK)
    testImplementation(Libs.SPRING_MOCKK)
    testImplementation(Libs.WIRE_MOCK)
}
