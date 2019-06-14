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

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import dev.razil.folio.GlideApp
import dev.razil.folio.core.Incomplete
import dev.razil.folio.core.Result
import dev.razil.folio.util.hide
import dev.razil.folio.util.show

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
    if (url.isNullOrBlank()) {
        return
    }
    GlideApp.with(this)
        .asBitmap()
        .load(url)
        .thumbnail(0.1f)
        .transition(BitmapTransitionOptions.withCrossFade())
        .centerCrop()
        .transform(RoundedCorners(15))
        .into(
            object : CustomViewTarget<ImageView, Bitmap>(this) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    hide()
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    show()
                    setImageBitmap(resource)
                }

            }
        )
}