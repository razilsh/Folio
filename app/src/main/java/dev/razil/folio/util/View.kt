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

package dev.razil.folio.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.razil.folio.util.Ext.ctx

fun Int.toDp(): Float {
    return this / (ctx.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.toPx(): Float {
    return this * (ctx.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun View.doBounceAnimation() {
    val mAnimatorSet = AnimatorSet()
    mAnimatorSet.playTogether(
        ObjectAnimator.ofFloat(this, "translationY", 60f, -15f, 0f),
        ObjectAnimator.ofFloat(this, "scaleY", 1.3f, 1f)
    )
    if (this is ViewGroup) {
        this.children.forEach {
            mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(it, "translationY", 60f, -15f, 0f),
                ObjectAnimator.ofFloat(it, "scaleY", .7f, 1f)
            )
        }
    }
    mAnimatorSet.interpolator = PathInterpolatorCompat.create(0.530f, 0.995f)
    mAnimatorSet.startDelay = 100
    mAnimatorSet.duration = 700
    mAnimatorSet.start()
}

fun getColor(@ColorRes color: Int): Int = ContextCompat.getColor(ctx, color)
fun getDrawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(ctx, resId)!!
fun getFont(@FontRes resId: Int): Typeface = ResourcesCompat.getFont(ctx, resId) ?: Typeface.DEFAULT
fun getInt(@IntegerRes resId: Int): Int = ctx.resources.getInteger(resId)
fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
    ctx.resources.getString(resId, formatArgs)

fun getString(@StringRes resId: Int): String = ctx.resources.getString(resId)

fun toast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ctx, text, duration).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(invisible: Boolean = false) {
    visibility = when (invisible) {
        true -> View.INVISIBLE
        false -> View.GONE

    }
}


fun Fragment.snack(
    text: String,
    duration: Int = Snackbar.LENGTH_LONG, @IdRes anchorView: Int = android.R.id.content
) {
    Snackbar.make(view!!, text, duration).setAnchorView(anchorView).show()
}


fun divider(@DrawableRes drawableRes: Int) =
    DividerItemDecoration(ctx, LinearLayoutManager.VERTICAL).apply {
        setDrawable(getDrawable(drawableRes))
    }


fun <T> Fragment.observe(value: LiveData<T>, callback: (T) -> Unit) {
    value.observe(viewLifecycleOwner, Observer {
        it ?: return@Observer
        callback(it)
    })
}

