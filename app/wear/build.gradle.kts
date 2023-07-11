plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // from https://developer.android.com/studio/build/build-variants#configure-sourcesets
    // and https://developer.android.com/studio/build/gradle-tips#change-default-source-set-configurations
    sourceSets["main"].res.setSrcDirs(listOf("../mobile/src/main/res", "src/main/res"))

    namespace = "app.pocketstats"
    compileSdk = 33

    defaultConfig {
        applicationId = "app.pocketstats"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.wear:wear-ongoing:1.0.0")
    implementation("com.google.android.horologist:horologist-compose-layout:0.3.2")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.wear.compose:compose-material:${rootProject.extra["wear_compose_version"]}")
    implementation("androidx.wear.compose:compose-foundation:${rootProject.extra["wear_compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.wear:wear:1.2.0")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${rootProject.extra["compose_version"]}")
}