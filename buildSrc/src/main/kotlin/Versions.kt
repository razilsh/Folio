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

import kotlin.String

/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val appcompat: String = "1.1.0-beta01" 

    const val browser: String = "1.0.0" 

    const val constraintlayout: String = "2.0.0-beta1" 

    const val core_ktx: String = "1.2.0-alpha01" 

    const val androidx_databinding: String = "3.4.1"

    const val fragment_ktx: String = "1.1.0-beta01" 

    const val androidx_lifecycle: String = "2.2.0-alpha01" 

    const val androidx_navigation: String = "2.1.0-alpha05" 

    const val androidx_paging: String = "2.1.0" 

    const val palette_ktx: String = "1.0.0" 

    const val androidx_room: String = "2.1.0-rc01" 

    const val espresso_core: String = "3.2.0" 

    const val androidx_test_ext_junit: String = "1.1.1" 

    const val com_android_tools_build_gradle: String = "3.4.1"

    const val lint_gradle: String = "26.4.1"

    const val com_github_brianplummer: String = "0.1.2" 

    const val com_github_bumptech_glide: String = "4.9.0" 

    const val jraw: String = "fa1efa3" // available: "1.1.0"

    const val com_github_piasy: String = "1.5.7" 

    const val exoplayer: String = "2.9.6" // available: "2.10.1"

    const val material: String = "1.1.0-alpha07" 

    const val com_google_dagger: String = "2.23.1"

    const val threetenabp: String = "1.2.1"

    const val timber: String = "4.7.1" 

    const val com_squareup_moshi: String = "1.8.0" 

    const val logging_interceptor: String = "3.14.2" 

    const val com_squareup_retrofit2: String = "2.6.1-SNAPSHOT"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val kohii: String = "1.0.0.2906-A06" 

    const val junit_junit: String = "4.12" 

    const val flick: String = "1.4.0" 

    const val inboxrecyclerview: String = "1.0.0-rc1" 

    const val jraw_android: String = "1.1.0" 

    const val org_jetbrains_kotlin: String = "1.3.31" 

    const val org_jetbrains_kotlinx: String = "1.2.1" // available: "1.2.1-1.3.40-eap-67"

    const val ru_noties_markwon: String = "3.0.1" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.4.1"

        const val currentVersion: String = "5.4.1"

        const val nightlyVersion: String = "5.6-20190609000024+0000"

        const val releaseCandidate: String = "5.5-rc-2"
    }
}
