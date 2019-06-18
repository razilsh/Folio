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

package dev.razil.folio.ui.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import dev.razil.folio.GlideApp
import dev.razil.folio.core.Incomplete
import dev.razil.folio.core.Result
import dev.razil.folio.core.data.Post
import dev.razil.folio.util.hasImageFileExtension
import dev.razil.folio.util.hide
import dev.razil.folio.util.show
import timber.log.Timber

@BindingAdapter("visibleIf")
fun View.visibleIf(predicate: Boolean = true) {
    visibility = when (predicate) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}


@BindingAdapter("visibleIf")
fun <T> View.visibleIf(request: Result<T>?) {
    if (request == null) {
        return
    }
    visibility = when (request) {
        is Incomplete -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(url: String?) {
    val target = object : CustomViewTarget<ImageView, Drawable>(this) {
        override fun onLoadFailed(errorDrawable: Drawable?) {


        }

        override fun onResourceCleared(placeholder: Drawable?) {

        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            setImageDrawable(resource)
        }

    }
    GlideApp.with(this).clear(target)
    if (url.isNullOrBlank()) {
        return
    }
    GlideApp.with(this)
        .load(url)
        .thumbnail(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(target)
}

@BindingAdapter("loadPostImage")
fun ImageView.loadPostImage(post: Post) {
    val url = post.url
    Timber.i("url = ${post.url}")
    Timber.i("hasImageFileExtension = ${url.hasImageFileExtension()}")
    if (url.hasImageFileExtension()) {
        show()
        GlideApp.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        GlideApp.with(this).clear(this)
        hide()
        return
    }
}
