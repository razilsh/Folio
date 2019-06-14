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

import dev.razil.folio.ui.posts.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Submission
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.pagination.DefaultPaginator
import javax.inject.Inject
import net.dean.jraw.models.Comment as JrawModelsComment

class PostRepository @Inject constructor(private val accountHelper: AccountHelper) {
    private val redditClient: RedditClient by lazy { getClient() }
    private val paginator: DefaultPaginator<Submission> by lazy {
        redditClient.frontPage()
            .limit(NETWORK_PAGE_SIZE)
            .build()
    }

    private fun getClient(userLess: Boolean = true): RedditClient = when (userLess) {
        true -> accountHelper.switchToUserless()
        else -> accountHelper.switchToUser(accountHelper.reddit.requireAuthenticatedUser())
    }

    suspend fun posts() = withContext(Dispatchers.IO) {
        paginator.next()
            .map {
                Post(
                    id = it.id,
                    author = it.author,
                    title = it.title,
                    score = it.score.toString(),
                    subreddit = it.subreddit,
                    thumbnail = it.thumbnail,
                    totalComments = it.commentCount.toString(),
                    url = it.url
                )
            }
    }

    companion object {
        private const val DB_PAGE_SIZE = 10
        private const val NETWORK_PAGE_SIZE = 10
    }
}