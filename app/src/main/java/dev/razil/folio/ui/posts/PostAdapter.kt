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

package dev.razil.folio.ui.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.razil.folio.core.data.Post
import dev.razil.folio.databinding.PostItemBinding
import dev.razil.folio.util.loadInTarget

class PostAdapter(private val onClick: (Post) -> Unit = {}) :
    PagedListAdapter<Post, PostAdapter.Holder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = PostItemBinding.inflate(inflater, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.pid?.toLong() ?: 0
    }

    inner class Holder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val textCreator = PostTextCreator()

        init {
            binding.root.setOnClickListener {
                val post = getItem(adapterPosition) ?: return@setOnClickListener
                onClick(post)
            }
        }

        fun bind(post: Post) {
            binding.executePendingBindings()
            if (post.submission.hasThumbnail()) {
                binding.thumbnailView.loadInTarget(post.submission.thumbnail)
            }

            textCreator.title(binding.titleView, post)
            textCreator.topText(binding.subredditView, post)
            textCreator.author(binding.authorView, post)
            textCreator.comment(binding.commentsView, post)
            textCreator.score(binding.scoreView, post)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem.sid == newItem.sid
        }

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem == newItem
        }
    }
}