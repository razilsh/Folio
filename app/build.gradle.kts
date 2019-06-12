/*
 * MIT License
 *
 * Copyright (c) 2019 Razil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "dev.razil.folio"
        minSdkVersion(23)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    dataBinding {
        isEnabled = true
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/atomicfu.kotlin_module")
    }
    androidExtensions {
        isExperimental = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlin_stdlib_jdk7)
    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    implementation(Libs.ru_noties_markwon_core)
    implementation(Libs.ext_strikethrough)
    implementation("ru.noties.markwon:ext-tables:3.0.1")
    implementation("ru.noties.markwon:html:3.0.1")

    implementation(Libs.jraw_android)
    implementation("com.commonsware.cwac:anddown:0.4.0")
    implementation("org.sufficientlysecure:html-textview:3.6")
    implementation("com.airbnb.android:epoxy:3.4.2")
    implementation("com.airbnb.android:epoxy-paging:3.4.2")
    implementation("com.airbnb.android:epoxy-databinding:3.4.2")
    kapt("com.airbnb.android:epoxy-processor:3.4.2")

    implementation(Libs.retrofit)
    implementation(Libs.converter_moshi)
    implementation(Libs.logging_interceptor)
    implementation(Libs.moshi)
    kapt(Libs.moshi_kotlin_codegen)

    implementation(Libs.material)
    implementation(Libs.browser)
    implementation(Libs.constraintlayout)
    implementation(Libs.appcompat)
    implementation(Libs.core_ktx)
    implementation(Libs.fragment_ktx)
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)
    implementation(Libs.room_runtime)
    implementation(Libs.room_ktx)
    kapt(Libs.room_compiler)
    implementation(Libs.paging_runtime_ktx)
    implementation(Libs.lifecycle_livedata_ktx)
    implementation(Libs.lifecycle_extensions)
    implementation(Libs.lifecycle_runtime_ktx)
    kapt(Libs.lifecycle_common_java8)

    implementation(Libs.palette_ktx)

    implementation(Libs.dagger)
    kapt(Libs.dagger_compiler)

    implementation(Libs.timber)
    implementation(Libs.threetenabp)

    implementation(Libs.glide)
    implementation(Libs.okhttp3_integration)
    kapt(Libs.com_github_bumptech_glide_compiler)
    implementation(Libs.glideimageloader)
    implementation(Libs.progresspieindicator)
    implementation(Libs.glideimageviewfactory)
    implementation(Libs.kohii)
    implementation(Libs.exoplayer)

    implementation(Libs.inboxrecyclerview)
    implementation(Libs.flick)
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.ibm.icu:icu4j:62.1")
    debugImplementation(Libs.tinydancer)
    releaseImplementation(Libs.tinydancer_noop)

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}

