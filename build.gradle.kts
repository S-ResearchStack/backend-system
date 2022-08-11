import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.SPRING_BOOT.name) version Plugins.SPRING_BOOT.version apply false
    id(Plugins.DEPENDENCY_MANAGEMENT.name) version Plugins.DEPENDENCY_MANAGEMENT.version apply false
    id(Plugins.GRADLE_KTLINT.name) version Plugins.GRADLE_KTLINT.version apply false
    id(Plugins.ARTURBOSCH_DETEKT.name) version Plugins.ARTURBOSCH_DETEKT.version apply false

    kotlin("jvm") version Versions.KOTLIN apply false
    kotlin("plugin.spring") version Versions.KOTLIN apply false
}

allprojects {
    group = "com.samsung.healthcare"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = Jvms.SOURCE_COMPATIBILITY
        targetCompatibility = Jvms.TARGET_COMPATIBILITY
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = Jvms.JVM_TARGET
            targetCompatibility = Jvms.JVM_TARGET
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    apply {
        plugin(Plugins.DEPENDENCY_MANAGEMENT.name)
        plugin(Plugins.GRADLE_KTLINT.name)
        plugin(Plugins.ARTURBOSCH_DETEKT.name)
    }
}
