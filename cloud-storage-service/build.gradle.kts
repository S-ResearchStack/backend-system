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
    implementation(Libs.SPRING_BOOT_STARTER_OAUTH2_CLIENT)
    implementation(Libs.REACTOR_KOTLIN_EXTENSION)
    implementation(platform(Libs.LIBRARIES_BOM))
    implementation(Libs.GOOGLE_CLOUD_STORAGE)
    implementation(Libs.AWS_JAVA_SDK)
    implementation(platform(Libs.AWSSDK_BOM))
    implementation(Libs.S3_TRANSFER_MANAGER)
    implementation(Libs.AZURE_STORAGE_BLOB)
    implementation(Libs.AZURE_STORAGE_COMMON)
    implementation(Libs.AZURE_IDENTITY)

    testImplementation(Libs.SPRING_BOOT_STARTER_TEST) {
        exclude(module = "mockito-core")
    }
    testImplementation(Libs.REACTOR_TEST)
    testImplementation(Libs.MOCKK)
    testImplementation(Libs.SPRING_MOCKK)
    testImplementation(Libs.WIRE_MOCK)
}
