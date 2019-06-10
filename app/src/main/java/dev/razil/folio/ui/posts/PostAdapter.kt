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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.razil.folio.R
import dev.razil.folio.core.data.Post
import dev.razil.folio.databinding.PostItemBinding
import dev.razil.folio.util.loadInTarget

class PostAdapter : ListAdapter<Post, PostAdapter.Holder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = PostItemBinding.inflate(inflater, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.post_item
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).pid
    }

    class Holder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val textCreator = PostTextCreator()

        fun bind(post: Post) {
            if (post.submission.hasThumbnail()) {
                binding.thumbnail.loadInTarget(post.submission.thumbnail)
            }

            textCreator.title(binding.title, post)
            textCreator.topText(binding.topText, post)
            textCreator.author(binding.author, post)
            textCreator.comment(binding.comments, post)
            textCreator.score(binding.score, post)
            // Important
            binding.executePendingBindings()
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