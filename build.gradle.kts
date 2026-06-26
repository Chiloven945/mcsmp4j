plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.jreleaser)
}

group = "top.chiloven"
version = "0.1.0-alpha.1"

description = "Java client library for Minecraft Server Management Protocol (MCSMP)."

val projectUrl = "https://github.com/Chiloven945/mcsmp4j"
val licenseName = "Apache-2.0"
val licenseUrl = "https://spdx.org/licenses/Apache-2.0.html"
val developerId = providers.gradleProperty("mcsmpDeveloperId").orElse("chiloven945")
val developerName = providers.gradleProperty("mcsmpDeveloperName").orElse("Chiloven945")
val developerEmail = providers.gradleProperty("mcsmpDeveloperEmail").orElse("")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(libs.jackson.core)
    api(libs.jackson.databind)
    compileOnlyApi(libs.jspecify)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:all,-missing", "-quiet")
        links("https://docs.oracle.com/en/java/javase/21/docs/api/")
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("mcsmp4j")
                description.set(project.description)
                url.set(projectUrl)
                inceptionYear.set("2026")

                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                        val email = developerEmail.get()
                        if (email.isNotBlank()) {
                            this.email.set(email)
                        }
                    }
                }

                scm {
                    connection.set("scm:git:$projectUrl.git")
                    developerConnection.set("scm:git:ssh://git@github.com/Chiloven945/mcsmp4j.git")
                    url.set(projectUrl)
                    tag.set("HEAD")
                }
            }
        }
    }

    repositories {
        maven {
            name = "staging"
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

jreleaser {
    configFile = layout.projectDirectory.file("jreleaser.yml")
    gitRootSearch = true
}

// JReleaser opens build/jreleaser/trace.log while Gradle is still preparing the task.
// On Windows this can make Gradle's stale-output cleanup fail before the task action
// starts with "Unable to delete ... build/jreleaser/trace.log". These tasks are
// release/deployment tasks and should always execute when requested, so disabling
// Gradle's output state tracking for them is safe and avoids the file-lock race.
tasks.matching { it.name.startsWith("jreleaser") }.configureEach {
    doNotTrackState("JReleaser writes trace.log under its output directory during task preparation; Gradle stale-output cleanup can lock-conflict on Windows.")
}

tasks.named("jreleaserDeploy") {
    dependsOn(tasks.named("publishMavenJavaPublicationToStagingRepository"))
}

tasks.register("stageMavenCentral") {
    group = "publishing"
    description = "Builds, tests, and stages Maven Central artifacts under build/staging-deploy."
    dependsOn(tasks.named("check"), tasks.named("publishMavenJavaPublicationToStagingRepository"))
}

tasks.register("releaseMavenCentral") {
    group = "publishing"
    description = "Stages artifacts and deploys them to Maven Central through JReleaser."
    dependsOn(tasks.named("jreleaserDeploy"))
}
