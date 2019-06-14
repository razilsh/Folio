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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.itemanimators.SlideUpAlphaAnimator
import dev.razil.folio.util.divider
import dev.razil.folio.util.onLoadMore
import kotlinx.android.synthetic.main.list_fragment.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@FlowPreview
class ListFragment : Fragment(R.layout.list_fragment) {
    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val viewModel by viewModels<PostViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = PostController()
        viewModel.posts.observe(viewLifecycleOwner) { (posts, isLoading) ->
            controller.setData(posts, isLoading)
        }
        rv_posts.setController(controller)
        rv_posts.addItemDecoration(divider(R.drawable.divider))
        rv_posts.itemAnimator = SlideUpAlphaAnimator()
        lifecycleScope.launchWhenCreated {
            rv_posts.onLoadMore(1).collect {
                viewModel.loadMore()
            }
        }
    }
}