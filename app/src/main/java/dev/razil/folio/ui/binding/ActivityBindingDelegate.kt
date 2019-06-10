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

package dev.razil.folio.ui.binding

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityBindingDelegate<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : ReadOnlyProperty<FragmentActivity, T> {
    private var binding: T? = null
    override operator fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T =
        binding ?: DataBindingUtil.setContentView<T>(thisRef, layoutResId)
            .also { it.lifecycleOwner = thisRef }
            .also { binding = it }
}

fun <T : ViewDataBinding> FragmentActivity.bind(@LayoutRes layoutResId: Int): ActivityBindingDelegate<T> =
    ActivityBindingDelegate(layoutResId)
