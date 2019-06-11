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

package dev.razil.folio.ui.posts

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.ibm.icu.text.CompactDecimalFormat
import dev.razil.folio.R
import dev.razil.folio.core.data.Post
import dev.razil.folio.util.Spanny
import dev.razil.folio.util.getColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*
import java.util.concurrent.Executors

class PostTextCreator : CoroutineScope by MainScope() {
    private val spanny = Spanny()
    private val blueGrey = getColor(R.color.blue_grey)
    private val grey = Color.parseColor("#757575")
    private val colorTextSecondary = getColor(R.color.colorTextSecondary)
    private val space = "\u0020"
    private val separator = "\u0020\u0020\uA78F\u0020\u0020"
    private val executor = Executors.newSingleThreadExecutor()
    private val numberFormatter = CompactDecimalFormat.getInstance(
        Locale.getDefault(),
        CompactDecimalFormat.CompactStyle.SHORT
    )

    fun title(textView: TextView, post: Post) {
        preCompute(textView, post.getTitle())
    }

    fun topText(textView: TextView, post: Post) {
        preCompute(textView, "r/${post.subreddit}")
    }


    fun author(textView: TextView, post: Post) {
        val author = post.submission.author
        preCompute(textView, author)
    }

    fun comment(textView: TextView, post: Post) {
        val comments = numberFormatter.format(post.submission.commentCount)
        preCompute(textView, comments)
    }

    private fun preCompute(textView: TextView, text: CharSequence) {
        val params = TextViewCompat.getTextMetricsParams(textView)
        val future = PrecomputedTextCompat.getTextFuture(text, params, executor)
        (textView as? AppCompatTextView)?.setTextFuture(future)
    }

    fun score(textView: TextView, post: Post) {
        val score = numberFormatter.format(post.score)
        preCompute(textView, score)
    }

}