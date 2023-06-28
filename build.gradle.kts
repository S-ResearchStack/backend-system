import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.SPRING_BOOT.name) version Plugins.SPRING_BOOT.version apply false
    id(Plugins.DEPENDENCY_MANAGEMENT.name) version Plugins.DEPENDENCY_MANAGEMENT.version apply false
    id(Plugins.GRADLE_KTLINT.name) version Plugins.GRADLE_KTLINT.version apply false
    id(Plugins.ARTURBOSCH_DETEKT.name) version Plugins.ARTURBOSCH_DETEKT.version apply false
    id("java")
    id("jacoco")

    kotlin("jvm") version Versions.KOTLIN apply false
    kotlin("plugin.spring") version Versions.KOTLIN apply false
    kotlin("kapt") version Versions.KOTLIN apply false
}

val jacocoExcludeFiles = mapOf(
    "kaptGenerated" to listOf("**/mapper/**/*Impl.*"),
    "antlrGenerated" to listOf(
        "**/branchlogicengine/BranchLogicLexer*",
        "**/branchlogicengine/BranchLogicParser*",
        "**/branchlogicengine/BranchLogicBase*",
    ),
)

tasks.register<JacocoReport>("jacocoRootReport") {
    subprojects {
        this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
            this@subprojects.tasks.matching {
                it.extensions.findByType<JacocoTaskExtension>() != null
            }.configureEach {
                sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
                executionData(this)
            }
        }
    }

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {file ->
            fileTree(file) {
                exclude(jacocoExcludeFiles.map{it.value}.flatten())
            }
        })
    )
}

allprojects {
    group = "com.samsung.healthcare"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
        plugin("jacoco")
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
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }

        classDirectories.setFrom(
            files(classDirectories.files.map {file ->
                fileTree(file) {
                    exclude(jacocoExcludeFiles.map{it.value}.flatten())
                }
            })
        )

        finalizedBy(tasks.jacocoTestCoverageVerification)
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.7500".toBigDecimal()
                }
            }
        }


        classDirectories.setFrom(
            files(classDirectories.files.map {file ->
                fileTree(file) {
                    exclude(jacocoExcludeFiles.map{it.value}.flatten())
                }
            })
        )

    }

    tasks.register<Test>("negativeTest") {
        useJUnitPlatform {
            includeTags("negative")
        }
    }

    tasks.register<Test>("positiveTest") {
        useJUnitPlatform {
            includeTags("positive")
        }
    }
}

subprojects {
    apply {
        plugin(Plugins.DEPENDENCY_MANAGEMENT.name)
        plugin(Plugins.GRADLE_KTLINT.name)
        plugin(Plugins.ARTURBOSCH_DETEKT.name)
    }
}
