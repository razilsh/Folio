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

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.data.Comment
import dev.razil.folio.util.KotlinEpoxyHolder
import ru.noties.markwon.utils.NoCopySpannableFactory

@EpoxyModelClass(layout = R.layout.comment_item)
abstract class CommentModel : EpoxyModelWithHolder<CommentModel.CommentHolder>() {

    @EpoxyAttribute
    lateinit var comment: Comment
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    override fun bind(holder: CommentHolder) {
        holder.author.text = comment.author
        when {
            comment.body.isNullOrBlank() -> return
            else -> {
                val body = comment.body!!
                holder.body.setSpannableFactory(NoCopySpannableFactory.getInstance())
                Folio.markwon().setMarkdown(holder.body, body)
            }
        }
    }

    class CommentHolder : KotlinEpoxyHolder() {
        val author: TextView by bind(R.id.author)
        val body: TextView by bind(R.id.body)
    }
}