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

import com.airbnb.epoxy.TypedEpoxyController
import dev.razil.folio.commentItem
import dev.razil.folio.commentPostItem
import dev.razil.folio.progressItem
import dev.razil.folio.core.data.Post
import dev.razil.folio.ui.widgets.IndentedLayout

class CommentController : TypedEpoxyController<Triple<Post, List<Comment>, Boolean>>() {
    override fun buildModels(data: Triple<Post, List<Comment>, Boolean>) {
        val (post, comments, isLoading) = data
        commentPostItem {
            id(post.id)
            post(post)
        }
        if (isLoading) {
            progressItem { id("loading${comments.size}") }
        }
        comments.forEach { comment ->
            commentItem {
                id(comment.id)
                comment(comment)
                onBind { model, view, position ->
                    (view.dataBinding.root as? IndentedLayout)?.setIndentationDepth(comment.depth - 1)
                }
            }
        }
    }
}