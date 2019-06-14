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

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibm.icu.text.CompactDecimalFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.flowViaChannel
import java.util.*

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
}.flowOn(Dispatchers.Default).debounce(500)

fun RecyclerView.LayoutManager.toLinearLayoutManager() = this as LinearLayoutManager

/**
 * Helper to handle pagination. Use this when you want to append a list of results at a given offset.
 * This is safer than just appending blindly to a list because it guarantees that the data gets added
 * at the offset it was requested at.
 *
 * This will replace *all contents* starting at the offset with the new list.
 * For example: [1,2,3].appendAt([4], 1) == [1,4]]
 */
fun <T : Any> List<T>.appendAt(other: List<T>?, offset: Int) =
    subList(0, offset.coerceIn(0, size)) + (other ?: emptyList())

fun <T> LiveData<T>.asFlow() = flowViaChannel<T?> {
    it.offer(value)
    val observer = Observer<T> { t -> it.offer(t) }
    observeForever(observer)
    it.invokeOnClose {
        removeObserver(observer)
    }
}

fun Number.formatCompact() = compactFormat(this.toString())

fun compactFormat(text: String): String? {
    if (text.isBlank()) return null
    val formatter = CompactDecimalFormat.getInstance(
        Locale.getDefault(),
        CompactDecimalFormat.CompactStyle.SHORT
    )
    return text.toInt().let(formatter::format)
}