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
import androidx.lifecycle.viewModelScope
import dev.razil.folio.core.repository.PostRepository
import dev.razil.folio.ui.comments.Comment
import dev.razil.folio.util.appendAt
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {
    init {
        loadMore()
    }

    private val _posts = MutableLiveData<Pair<List<Post>, Boolean>>(Pair(emptyList(), true))
    val posts: LiveData<Pair<List<Post>, Boolean>>
        get() = _posts

    private val _comments = MutableLiveData<Triple<Post, List<Comment>, Boolean>>()
    val comments: LiveData<Triple<Post, List<Comment>, Boolean>>
        get() = _comments

    fun loadMore() {
        load()
    }

    private fun load() = viewModelScope.launch {
        val c = _posts.value?.first ?: emptyList()
        _posts.postValue(_posts.value?.copy(c, true))
        val new = repository.posts()
        val d = _posts.value?.copy(first = c.appendAt(new, c.size), second = false)
        _posts.postValue(d)
    }

    fun loadComments(postId: String) = viewModelScope.launch {
        val triple = Triple(getSelectedPost(postId)!!, emptyList<Comment>(), true)
        _comments.postValue(triple)
        val next = repository.comments(postId)
        _comments.postValue(triple.copy(second = next, third = false))
    }

    fun getSelectedPost(id: String): Post? {
        return posts.value?.first?.find { it.id == id }
    }
}