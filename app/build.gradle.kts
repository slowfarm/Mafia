plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.eva.inc.mafia"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eva.inc.mafia"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    applicationVariants.all {
        assembleProvider.get().doLast {
            copy {
                val oldName = "app-$flavorName-${buildType.name}.apk"
                val newName = "$applicationId.${buildType.name}.apk"
                val apkPath = "app/build/outputs/apk/$flavorName/${buildType.name}/$oldName"

                from("${project.parent?.projectDir}/$apkPath")
                into("${project.parent?.projectDir}/outputs")

                rename { fileName -> fileName.replace(oldName, newName) }
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}