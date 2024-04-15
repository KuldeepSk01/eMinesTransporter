plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")

}

android {
    namespace = "com.emines_transportation"
    compileSdk = 34
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.emines_transportation"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "1.7" //updates on 15Feb2024 10April2024 5(1.4)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        /*keystore
        * Key alias : EminesTransporter
        * file :  eMinesTransporter.jks
        * password : EminesTransporter
        * */
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    //implementation("com.google.android.gms:play-services-location:21.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //ssp and sdp library for measure of width and height size according to screen
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //glide library for showing image from url
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //koin is for inject the dependency of any class with easy way
    val koinAndroidVersion = "3.5.0"
    implementation("io.insert-koin:koin-android:$koinAndroidVersion")
    implementation("io.insert-koin:koin-core:$koinAndroidVersion")
    testImplementation("io.insert-koin:koin-test:$koinAndroidVersion")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp:3.14.9")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
// GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    //dexter
    implementation ("com.karumi:dexter:6.2.3")
    //google map library
    //implementation("com.google.android.gms:play-services-maps:18.2.0")

    //for zoomInZoomOut image library
   // implementation ("com.github.MikeOrtiz:TouchImageView:1.4.1")


    //for custom calender


//for auto update application version when versioncode is upgraded
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")



}