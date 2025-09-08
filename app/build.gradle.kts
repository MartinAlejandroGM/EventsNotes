import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }

    namespace = "com.andro_sk.eventnotes"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.andro_sk.eventnotes"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.value(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.bundles.compose.essentials)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.navigation.essentials)
    implementation(libs.material)
    implementation(libs.landscapist.glide)
    implementation(libs.bundles.hilt.essentials)
    implementation(libs.bundles.room.essentials)
    implementation(libs.androidx.datastore)
    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

