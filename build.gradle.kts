plugins {
    `java-library`
    `maven-publish`
}

group = "top.chiloven"
version = "0.1.0-SNAPSHOT"

description = "Java client library for Minecraft Server Management Protocol (MCSMP)."

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
                url.set("https://github.com/Chiloven945/mcsmp4j")

                licenses {
                    license {
                        name.set("Apache-2.0 License")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("chiloven945")
                        name.set("Chiloven945")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/Chiloven945/mcsmp4j.git")
                    developerConnection.set("scm:git:ssh://git@github.com/Chiloven945/mcsmp4j.git")
                    url.set("https://github.com/Chiloven945/mcsmp4j")
                }
            }
        }
    }

    repositories {
        maven {
            name = "localStaging"
            url = layout.buildDirectory.dir("repos/releases").get().asFile.toURI()
        }
    }
}
