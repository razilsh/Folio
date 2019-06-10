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

package dev.razil.folio.core.repository

import dev.razil.folio.core.data.FolioDatabase
import dev.razil.folio.core.data.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dean.jraw.RedditClient
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val accountHelper: AccountHelper,
    private val database: FolioDatabase
) {
    private val redditClient: RedditClient by lazy { getClient() }
    private val submissionPaginator by lazy { redditClient.frontPage().limit(10).build() }

    suspend fun listing() = withContext(Dispatchers.IO) {
        val page = submissionPaginator.next()
        page.mapIndexed { index, submission ->
            val pid = index.toLong()
            Post(pid, submission.id, submission)
        }
    }

    fun refresh() = submissionPaginator.restart()

    private fun getClient(userLess: Boolean = true): RedditClient = when (userLess) {
        true -> accountHelper.switchToUserless()
        else -> accountHelper.switchToUser(accountHelper.reddit.requireAuthenticatedUser())
    }
}