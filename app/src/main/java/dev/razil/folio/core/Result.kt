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

package dev.razil.folio.core

import java.util.Arrays

/**
 * The T generic is unused for some classes but since it is sealed and useful for Success and Fail,
 * it should be on all of them.
 *
 * Complete: Success, Fail
 * ShouldLoad: Uninitialized, Fail
 */
sealed class Result<out T>(val complete: Boolean, val shouldLoad: Boolean) {

    /**
     * Returns the Success value or null.
     *
     * Can be invoked as an operator like: `yourProp()`
     */
    open operator fun invoke(): T? = null

    companion object {
        /**
         * Helper to set metadata on a Success instance.
         * @see Success.metadata
         */
        fun <T> Success<*>.setMetadata(metadata: T) {
            this.metadata = metadata
        }

        /**
         * Helper to get metadata on a Success instance.
         * @see Success.metadata
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> Success<*>.getMetadata(): T? = this.metadata as T?
    }
}

object Uninitialized : Result<Nothing>(complete = false, shouldLoad = true), Incomplete

class Loading<out T> : Result<T>(complete = false, shouldLoad = false), Incomplete {
    override fun equals(other: Any?) = other is Loading<*>

    override fun hashCode() = "Loading".hashCode()
}

data class Success<out T>(private val value: T) : Result<T>(complete = true, shouldLoad = false) {

    override operator fun invoke(): T = value

    /**
     * Optional information about the value.
     * This is intended to support tooling (eg logging).
     * It allows data about the original Observable to be kept and accessed later. For example,
     * you could map a network request to just the data you need in the value, but your base layers could
     * keep metadata about the request, like timing, for logging.
     *
     * @see BaseMvRxViewModel.execute
     * @see Result.setMetadata
     * @see Result.getMetadata
     */
    internal var metadata: Any? = null
}

data class Fail<out T>(val error: Throwable) : Result<T>(complete = true, shouldLoad = true) {
    override fun equals(other: Any?): Boolean {
        if (other !is Fail<*>) return false

        val otherError = other.error
        return error::class == otherError::class &&
            error.message == otherError.message &&
            error.stackTrace.firstOrNull() == otherError.stackTrace.firstOrNull()
    }

    override fun hashCode(): Int = Arrays.hashCode(arrayOf(error::class, error.message, error.stackTrace[0]))
}

/**
 * Helper interface for using Async in a when clause for handling both Uninitialized and Loading.
 *
 * With this, you can do:
 * when (data) {
 *     is Incomplete -> Unit
 *     is Success    -> Unit
 *     is Fail       -> Unit
 * }
 */
interface Incomplete