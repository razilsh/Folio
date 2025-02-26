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

package dev.razil.folio.core.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import dev.razil.folio.core.Fail
import dev.razil.folio.core.Loading
import dev.razil.folio.core.Result
import dev.razil.folio.core.Success
import dev.razil.folio.core.Uninitialized
import dev.razil.folio.core.data.Comment
import dev.razil.folio.core.data.FolioDatabase
import dev.razil.folio.core.data.Post
import dev.razil.folio.core.data.PostBoundaryCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dean.jraw.RedditClient
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.tree.CommentNode
import java.io.IOException
import javax.inject.Inject
import net.dean.jraw.models.Comment as JrawModelsComment

class PostRepository @Inject constructor(
    private val accountHelper: AccountHelper,
    private val database: FolioDatabase
) {
    private val redditClient: RedditClient by lazy { getClient() }
    private val submissionPaginator by lazy { redditClient.frontPage().limit(10).build() }

    val networkStatus = MutableLiveData<Result<Unit>>(Uninitialized)
    val mediator = MediatorLiveData<Result<Unit>>()

    suspend fun listing() = withContext(Dispatchers.IO) {
        val page = submissionPaginator.next()
        page.mapIndexed { index, submission ->
            Post(index, submission.id, submission)
        }
    }

    suspend fun refresh() = withContext(Dispatchers.IO) {
        if (networkStatus.value is Loading) {
            return@withContext
        }
        networkStatus.postValue(Loading())
        try {
            submissionPaginator.restart()
            val page = submissionPaginator.next()
            val posts = page.map { Post(sid = it.id, submission = it) }
            database.runInTransaction {
                database.posts().clear()
                database.posts().insert(posts)
            }
            networkStatus.postValue(Success(Unit))
        } catch (e: Exception) {
            networkStatus.postValue(Fail(e))
        }
    }

    suspend fun loadMore() = withContext(Dispatchers.IO) {
        if (networkStatus.value is Loading) {
            return@withContext
        }
        networkStatus.postValue(Loading())
        try {
            val page = submissionPaginator.next()
            val posts = page.map { Post(sid = it.id, submission = it) }
            database.runInTransaction {
                database.posts().insert(posts)
            }
            networkStatus.postValue(Success(Unit))
        } catch (e: IOException) {
            networkStatus.postValue(Fail(e))
        }
    }


    suspend fun loadMore(post: Post? = null, boundaryCallback: PostBoundaryCallback) =
        withContext(Dispatchers.IO) {
            database.posts()
                .getAll()
                .toLiveData(pageSize = DB_PAGE_SIZE, boundaryCallback = boundaryCallback)
        }

    private fun getClient(userLess: Boolean = true): RedditClient = when (userLess) {
        true -> accountHelper.switchToUserless()
        else -> accountHelper.switchToUser(accountHelper.reddit.requireAuthenticatedUser())
    }

    suspend fun getComments(submissionId: String) = withContext(Dispatchers.IO) {
        val root = redditClient.submission(submissionId).comments()
        root.walkTree().drop(1).toList().mapIndexed { index, commentNode ->
            val node = commentNode as CommentNode<JrawModelsComment>
            Comment(index, submissionId, node)
        }
    }

    companion object {
        private const val DB_PAGE_SIZE = 10
    }
}