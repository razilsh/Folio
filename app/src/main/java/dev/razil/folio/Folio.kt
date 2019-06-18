/*
 * MIT License
 *
 * Copyright (postChannel) 2019 Razil
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

package dev.razil.folio

import android.app.Application
import com.codemonkeylabs.fpslibrary.TinyDancer
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import dev.razil.folio.core.di.DaggerAppComponent
import dev.razil.folio.util.Ext
import dev.razil.folio.util.ImageLoader
import ru.noties.markwon.Markwon
import ru.noties.markwon.ext.strikethrough.StrikethroughPlugin
import ru.noties.markwon.ext.tables.TablePlugin
import ru.noties.markwon.html.HtmlPlugin

import timber.log.Timber

class Folio : Application() {
    private val appComponent by lazy { DaggerAppComponent.factory().create(this) }
    val markwon by lazy {
        Markwon.builder(this).usePlugins(
            listOf(
                StrikethroughPlugin.create(),
                HtmlPlugin.create(),
                TablePlugin.create(this)
            )
        ).build()
    }

    override fun onCreate() {
        super.onCreate()
        folio = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Ext.with(this)
        BigImageViewer.initialize(GlideImageLoader.with(this))
        TinyDancer.create().show(this)
        ImageLoader(this)
    }


    companion object {
        private lateinit var folio: Folio
        fun injector() = folio.appComponent
        fun markwon() = folio.markwon
    }
}
