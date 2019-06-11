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

package dev.razil.folio.core.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import net.dean.jraw.JrawUtils
import net.dean.jraw.models.PublicContribution
import net.dean.jraw.models.Submission
import net.dean.jraw.references.SubmissionReference

@Entity
@TypeConverters(EntityConverter::class)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val pid: Int = 0,
    val sid: String,
    val submission: Submission
) : PublicContribution<SubmissionReference> by submission {
    fun getTitle(): String = submission.title ?: "N/A"
    fun getCommentCount(): Int = submission.commentCount ?: 0
}

@Dao
interface PostDao {
    @Query("SELECT * FROM Post ORDER BY pid")
    fun getAll(): DataSource.Factory<Int, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Post>)

    @Query("DELETE FROM Post")
    fun clear()

}

class EntityConverter {
    private val adapter = JrawUtils.moshi.adapter(Submission::class.java).serializeNulls()

    @TypeConverter
    fun fromJson(json: String): Submission? {
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun toJson(submission: Submission): String? {
        return adapter.toJson(submission)
    }
}