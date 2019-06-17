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

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.ui.posts.PostViewModel
import kotlinx.android.synthetic.main.comment_fragment.*
import timber.log.Timber
import javax.inject.Inject

class CommentFragment : Fragment(R.layout.comment_fragment) {
    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel by activityViewModels<PostViewModel> { viewModelFactory }
    private val controller = CommentController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_comments.setController(controller)
        viewModel.comments.observe(viewLifecycleOwner) {
            // Will only load image if post.type == PostType.IMAGE
           // mediaImage.loadPostImage(it.first)
            controller.setData(it)
        }
    }
}