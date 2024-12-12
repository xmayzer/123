buildscript {
    repositories {
        google()
    }
    dependencies {
        val navVersion = "2.7.5"

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id ("com.android.library") version "7.3.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id ("androidx.navigation.safeargs.kotlin") version "2.7.5" apply false

}