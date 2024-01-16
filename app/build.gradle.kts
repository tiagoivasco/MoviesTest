import java.io.FileInputStream
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

        buildConfigField("String", "THE_MOVIE_DB_API_KEY", "\"" + getApiKey() + "\"")

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
    buildFeatures {
        viewBinding =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.koinAndroidx)
    implementation(Libs.androidXConstraintLayout)
    implementation(Libs.googleMaterial)
    implementation(Libs.retrofit)
    implementation(Libs.moshi)
    implementation(Libs.viewModels)
    implementation(Libs.androidxCore)
    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)
    implementation(Libs.swipeRefreshLayout)
    testImplementation(Libs.junit4)
    testImplementation(Libs.testMockk)
    testImplementation(Libs.testMockkInstrumented)
    androidTestImplementation(Libs.junitExt)
    androidTestImplementation(Libs.espresso)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.4.1")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("android.arch.lifecycle:extensions:1.1.1")
    testImplementation("org.mockito:mockito-core:2.28.2")
    androidTestImplementation("org.mockito:mockito-android:2.28.2")
    testImplementation("org.mockito:mockito-inline:2.28.2")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

fun getApiKey(): String {
    val props = Properties()
    props.load(FileInputStream(File("keystore.properties")))
    return props["THE_MOVIE_DB_API_KEY"].toString()
}