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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.google.android.material.appbar.CollapsingToolbarLayout
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.ui.binding.loadPostImage
import dev.razil.folio.ui.posts.PostViewModel
import kotlinx.android.synthetic.main.comment_fragment.*
import javax.inject.Inject

class CommentFragment : Fragment() {
    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel by activityViewModels<PostViewModel> { viewModelFactory }
    private val controller = CommentController()
    private lateinit var mediaImage: ImageView

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val content = inflater.inflate(R.layout.comment_fragment, container, false) as ViewGroup
        val ctl = content.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)

        // TODO show video layout when post type is video.
        mediaImage = inflater.inflate(R.layout.media_image, ctl, false) as ImageView
        ctl.addView(mediaImage)
        return content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_comments.setController(controller)
        viewModel.comments.observe(viewLifecycleOwner) {
            // Will only load image if post.type == PostType.IMAGE
            mediaImage.loadPostImage(it.first)
            controller.setData(it)
        }
    }
}