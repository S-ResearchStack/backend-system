import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
    antlr
}

dependencies {
    antlr(Libs.ANTLR)
    testImplementation(Libs.SPRING_BOOT_STARTER_TEST)
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.generateGrammarSource)
}

tasks.withType<Test> {
    dependsOn(tasks.generateGrammarSource)
}

tasks.generateGrammarSource {
    arguments.plusAssign(listOf("-visitor"))
    outputDirectory = file("build/generated/sources/antlr/main/java/com/samsung/healthcare/branchlogicengine")
}

sourceSets.main {
    java.srcDirs("build/generated/sources/antlr/main/java")
}

val bootJar: BootJar by tasks
bootJar.enabled = false
