@file:Suppress("UNUSED_VARIABLE")

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform") version kotlinVersion
    `maven-publish`
}

group = "com.harry1453.klaunchpad"
version = "0.0.6"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilations.forEach {
            it.kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }
    js(IR) {
        browser {
            dceTask {
                keep("KLaunchpad.connectToLaunchpadMK2", "KLaunchpad.connectToLaunchpadPro", "KLaunchpad.BLACK", "KLaunchpad.color", "KLaunchpad.faderSettings")
            }
            testTask {
                enabled = false
            }
        }
        binaries.executable()
        binaries.library()
    }
    mingwX64("nativeWindows") {
        compilations.getByName("main") {
            val nativeTypes by cinterops.creating {
                defFile(project.file("src/nativeApiMain/resources/NativeTypes.def"))
                packageName("nativeApi.types")
            }
        }
        binaries {
            sharedLib {
                baseName = "KLaunchpad"
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                languageSettings.optIn("kotlin.ExperimentalStdlibApi")
                languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
                languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                languageSettings.optIn("kotlin.js.ExperimentalJsExport")
                languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeApiMain by creating {
            dependsOn(commonMain.get())
        }

        val nativeWindowsMain by getting {
            dependsOn(nativeApiMain)
        }
    }
}

tasks {
    val preparePublishNpm by creating {
        dependsOn("compileProductionLibraryKotlinJs")
        doLast {
            delete {
                "$projectDir/build/js/packages/klaunchpad/package.json"
            }
            copy {
                from("$projectDir/src/jsMain/resources/package.json")
                from("$projectDir/README.md")
                into("$projectDir/build/js/packages/klaunchpad/")
            }
            copy {
                from("$projectDir/src/jsMain/resources/KLaunchpad.d.ts")
                into("$projectDir/build/js/packages/klaunchpad/kotlin")
            }
            val path = "$projectDir/build/js/packages/klaunchpad/".replace("\\", File.separator).replace("/", File.separator)
            println()
            println("Ready to publish! When you are ready to publish, enter the following commands:")
            println()
            println("cd \"$path\"")
            println("npm publish")
            println()
        }
    }

    val packageForPureJS by creating {
        dependsOn("jsBrowserProductionWebpack")
        doLast {
            println("Pure JS library created at: $projectDir/build/distributions/klaunchpad.js")
        }
    }
}

// Reduces Kotlin/JS bundle size
@Suppress("SuspiciousCollectionReassignment")
tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xir-per-module", "-Xir-property-lazy-initialization")
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
