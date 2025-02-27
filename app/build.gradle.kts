plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.turismotfg"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.turismotfg"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-firestore:23.0.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //MapsForge dependencies
    implementation("org.osmdroid:osmdroid-android:6.1.18");
    implementation ("androidx.preference:preference:1.2.0");
    //implementation("org.mapsforge:mapsforge-core:0.20.0")
    //implementation("org.mapsforge:mapsforge-map:0.20.0")
    //implementation("org.mapsforge:mapsforge-map-reader:0.20.0")
    //implementation("org.mapsforge:mapsforge-themes:0.20.0")
    //implementation("org.mapsforge:mapsforge-map-android:0.20.0")
    //implementation("com.caverock:androidsvg:1.4")
    //implementation("org.mapsforge:mapsforge-core:0.20.0")
    //implementation("org.mapsforge:mapsforge-poi:0.20.0")
    //implementation("org.mapsforge:mapsforge-poi-android:0.20.0")
    implementation ("com.squareup.picasso:picasso:2.71828");


}