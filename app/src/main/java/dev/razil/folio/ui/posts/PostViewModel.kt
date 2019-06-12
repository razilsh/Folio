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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dev.razil.folio.core.data.Comment
import dev.razil.folio.core.data.Post
import dev.razil.folio.core.data.PostBoundaryCallback
import dev.razil.folio.core.event.Event
import dev.razil.folio.core.repository.PostRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
class PostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val cb = PostBoundaryCallback(repository)
    val posts: LiveData<PagedList<Post>> = liveData {
        emitSource(repository.loadMore(boundaryCallback = cb))
    }

    val comments = MutableLiveData<Event<Pair<Post, List<Comment>>>>()

    fun onClick(post: Post) {
        val p = comments.value?.peek()?.first
        if (p != null && p == post) {
            return
        }
        comments.postValue(Event(Pair(post, emptyList())))
        viewModelScope.launch {
            comments.postValue(Event(Pair(post, repository.getComments(post.sid))))
        }
    }
}