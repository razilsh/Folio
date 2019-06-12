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

package dev.razil.folio.ui.comments

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dev.razil.folio.R
import dev.razil.folio.core.data.Post
import dev.razil.folio.ui.posts.PostTextCreator
import dev.razil.folio.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.comment_post)
abstract class PostModel : EpoxyModelWithHolder<PostModel.PostHolder>() {
    @EpoxyAttribute
    lateinit var post: Post

    @EpoxyAttribute
    lateinit var textCreator: PostTextCreator

    override fun bind(holder: PostHolder) {
        with(textCreator) {
            topText(holder.subreddit, post)
            title(holder.title, post)
            score(holder.score, post)
            author(holder.author, post)
            comment(holder.comments, post)
        }
    }

    class PostHolder : KotlinEpoxyHolder() {
        val subreddit by bind<TextView>(R.id.subredditView)
        val title by bind<TextView>(R.id.titleView)
        val score by bind<TextView>(R.id.scoreView)
        val author by bind<TextView>(R.id.authorView)
        val comments by bind<TextView>(R.id.commentsView)
    }
}