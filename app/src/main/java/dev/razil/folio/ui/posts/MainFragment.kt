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

package dev.razil.folio.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.razil.folio.Folio
import dev.razil.folio.R
import dev.razil.folio.core.data.Post
import dev.razil.folio.core.di.DaggerViewModelFactory
import dev.razil.folio.databinding.MainFragmentBinding
import dev.razil.folio.itemanimators.SlideUpAlphaAnimator
import dev.razil.folio.ui.binding.bind
import dev.razil.folio.ui.comments.CommentFragment
import dev.razil.folio.util.divider
import me.saket.inboxrecyclerview.InboxRecyclerView
import javax.inject.Inject

class MainFragment : Fragment() {
    init {
        Folio.injector().inject(this)
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private val binding: MainFragmentBinding by bind(R.layout.main_fragment)
    private val viewModel by activityViewModels<PostViewModel> { viewModelFactory }
    private val commentFragment = CommentFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClick: (Post) -> Unit = {
            viewModel.onClick(it)
            binding.submissionsView.expandItem(it.pid.toLong())
            childFragmentManager
                .beginTransaction()
                .replace(R.id.expandablePage, commentFragment)
                .commitAllowingStateLoss()
        }

        val postAdapter = PostAdapter(onClick)
        binding.submissionsView.setupWith(postAdapter.also { it.setHasStableIds(true) })
        viewModel.posts.observe(viewLifecycleOwner) { list ->
            postAdapter.submitList(list)
            binding.progressBar.hide()
        }

        requireActivity().addBackPressCallback()
    }

    fun FragmentActivity.addBackPressCallback() {
        onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.expandablePage.isExpandedOrExpanding) {
                binding.submissionsView.collapse()
            } else {
                if (!findNavController().navigateUp()) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    fun InboxRecyclerView.setupWith(postAdapter: PostAdapter) {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        itemAnimator = SlideUpAlphaAnimator()
        addItemDecoration(divider(R.drawable.divider))
        setExpandablePage(binding.expandablePage)
        adapter = postAdapter

    }
}