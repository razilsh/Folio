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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.Fail
import dev.razil.folio.core.Incomplete
import dev.razil.folio.core.Success
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.databinding.MainFragmentBinding
import dev.razil.folio.itemanimators.SlideUpAlphaAnimator
import dev.razil.folio.itemanimators.wia.SlideInUpAnimator
import dev.razil.folio.ui.binding.bind
import dev.razil.folio.util.divider
import dev.razil.folio.util.toast
import javax.inject.Inject

class MainFragment : Fragment() {
    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val binding: MainFragmentBinding by bind(R.layout.main_fragment)
    private val viewModel by viewModels<PostViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        
        val postAdapter = PostAdapter().apply { setHasStableIds(true) }
        binding.submissionsView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            itemAnimator = SlideUpAlphaAnimator()
            addItemDecoration(divider(R.drawable.divider))
        }

        viewModel.posts.observe(viewLifecycleOwner, Observer { request ->
            when (request) {
                is Incomplete -> return@Observer
                is Success -> postAdapter.submitList(request())
                is Fail -> toast(request.error.toString(), Toast.LENGTH_LONG)
            }
        })

        binding.submissionsView.adapter = postAdapter
    }
}