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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package dev.razil.folio.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import dev.razil.folio.core.Result
import dev.razil.folio.core.data.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.flowViaChannel

fun RecyclerView.onLoadMore(threshold: Int = 3) = flowViaChannel<Unit> { channel ->
    val linearLayoutManager = layoutManager!!.toLinearLayoutManager()
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val total = linearLayoutManager.itemCount
            val pos = linearLayoutManager.findLastVisibleItemPosition()
            total <= (pos + threshold) || return
            channel.offer(Unit)
        }
    }
    addOnScrollListener(scrollListener)
    channel.invokeOnClose { removeOnScrollListener(scrollListener) }
}.flowOn(Dispatchers.Default)

fun RecyclerView.LayoutManager.toLinearLayoutManager() = this as LinearLayoutManager

fun ImageView.loadInTarget(url: String?) {
    val target = object : CustomViewTarget<ImageView, Drawable>(this) {
        override fun onLoadFailed(errorDrawable: Drawable?) {
            visibility = View.GONE
        }

        override fun onResourceCleared(placeholder: Drawable?) {}

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            setImageDrawable(resource)
        }

    }

    target.clearOnDetach()
    if (url.isNullOrBlank()) {
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE
    Glide.with(this).load(url).into(target)
}

typealias PostRequest = LiveData<Result<List<Post>>>

fun <T> LiveData<T>.asFlow() = flowViaChannel<T?> {
    it.offer(value)
    val observer = Observer<T> { t -> it.offer(t) }
    observeForever(observer)
    it.invokeOnClose {
        removeObserver(observer)
    }
}