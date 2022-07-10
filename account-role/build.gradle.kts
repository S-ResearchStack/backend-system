plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(Libs.SPRING_BOOT_STARTER_OAUTH2_CLIENT)
    implementation(Libs.JACKSON_MODULE_KOTLIN)
    implementation(Libs.REACTOR_KOTLIN_EXTENSION)
    implementation(Libs.SPRING_BOOT_STARTER_AOP)

    testImplementation(Libs.SPRING_BOOT_STARTER_TEST) {
        exclude(module = "mockito-core")
    }
    testImplementation(Libs.REACTOR_TEST)
    testImplementation(Libs.MOCKK)
}
