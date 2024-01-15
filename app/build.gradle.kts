import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ivasco.moviestest"
    compileSdk = 34

    val properties = Properties()
    if (project.rootProject.file("local.properties").exists()) {
        properties.load(project.rootProject.file("local.properties").inputStream())
    }

    defaultConfig {
        applicationId = "com.ivasco.moviestest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            resValue ("string", "tmdb_key", properties.getProperty("tmdb.key", ""))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.koinAndroidx)
    implementation(Libs.androidXConstraintLayout)
    implementation(Libs.googleMaterial)
    implementation(Libs.androidxAppCompat)
    implementation(Libs.androidxCore)
    implementation(project(Modules.utils))
    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)
    testImplementation(Libs.junit4)
    testImplementation(Libs.testMockk)
    testImplementation(Libs.testMockkInstrumented)
    androidTestImplementation(Libs.junitExt)
    androidTestImplementation(Libs.espresso)
}