plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
//Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    // for ksp
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.beekeeper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.beekeeper"
        minSdk = 28
        targetSdk = 33
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
            buildConfigField("String","POSTMAN_BASE_URL","\"https://rameRealuri\"")
            buildConfigField("String","WEATHER_API_BASE_URL","\"https://rameRealuri\"")
        }
        debug {
            buildConfigField("String","POSTMAN_BASE_URL","\"https://82659fa2-2062-43a1-a804-1f7621f54e3a.mock.pstmn.io\"")
            buildConfigField("String","WEATHER_API_BASE_URL","\"https://api.openweathermap.org/data/2.5/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}
//Hilt
// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("com.github.Gruzer:simple-gauge-android:0.3.1")// for damage indicator

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")// for charts

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")// Firebase storage

    implementation("androidx.viewpager2:viewpager2:1.0.0") // viewPager2
    implementation("me.relex:circleindicator:2.1.6") // for circle indicator

    implementation("androidx.work:work-runtime-ktx:2.9.0")//for workManager
    implementation("androidx.hilt:hilt-work:1.2.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")
    ksp("androidx.room:room-compiler:2.6.1")// for room

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")// for okHttp logger interceptor


    //Datastore
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")// for DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("com.google.firebase:firebase-common-ktx:20.4.2")// for Hilt
    kapt("com.google.dagger:hilt-android-compiler:2.47")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit

    implementation ("com.squareup.moshi:moshi:1.9.3") // Moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.9.3") // For Kotlin support

    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")// Retrofit Converter for Moshi



    implementation("com.github.bumptech.glide:glide:4.16.0") // for glide


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // coroutines


    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // For ViewModel
    implementation ("androidx.activity:activity-ktx:1.8.2") // For ViewModel

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7") // for navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")// for navigation

    implementation("androidx.fragment:fragment-ktx:1.6.2")//for fragment

    implementation("androidx.recyclerview:recyclerview-selection:1.1.0") // for recycler selection


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging")

    implementation ("androidx.activity:activity-ktx:1.8.2")

    implementation("com.google.ai.client.generativeai:generativeai:0.2.2")

    implementation ("androidx.camera:camera-core:1.3.2")
    implementation ("androidx.camera:camera-camera2:1.3.2")
    implementation ("androidx.camera:camera-lifecycle:1.3.2")
    implementation ("androidx.camera:camera-video:1.3.2")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("androidx.camera:camera-view:1.3.2")
    implementation ("androidx.camera:camera-extensions:1.3.2")
    implementation ("androidx.activity:activity-ktx:1.8.2")




}