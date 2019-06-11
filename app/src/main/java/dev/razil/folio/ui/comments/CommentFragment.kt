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

package dev.razil.folio.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.databinding.CommentFragmentBinding
import dev.razil.folio.ui.binding.bind
import dev.razil.folio.ui.posts.PostTextCreator
import dev.razil.folio.ui.posts.PostViewModel
import dev.razil.folio.util.hide
import dev.razil.folio.util.loadInTarget
import dev.razil.folio.util.show
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentFragment : Fragment() {

    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel by activityViewModels<PostViewModel> { viewModelFactory }
    val binding by bind<CommentFragmentBinding>(R.layout.comment_fragment)
    private val textCreator = PostTextCreator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.postFlow().collect { post ->
                binding.executePendingBindings()
                textCreator.title(binding.titleView, post)
                textCreator.topText(binding.subredditView, post)
                textCreator.author(binding.authorView, post)
                textCreator.comment(binding.commentsView, post)
                textCreator.score(binding.scoreView, post)
                binding.post = post
                if (post.submission.postHint == "image") {
                    binding.imageView.show()
                    binding.imageView.loadInTarget(post.submission.url)
                } else {
                    binding.imageView.hide()
                }
            }
        }
    }
}