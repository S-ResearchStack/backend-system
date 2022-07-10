plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.named("jar") {
    enabled = false
}

dependencies {
    implementation(Libs.SPRING_BOOT_STARTER_WEB)
    implementation(Libs.KOTLIN_REFLECT)
    implementation(Libs.GSON)
    implementation(Libs.KOTLIN_LOGGING)
    testImplementation(Libs.SPRING_BOOT_STARTER_TEST)
}
