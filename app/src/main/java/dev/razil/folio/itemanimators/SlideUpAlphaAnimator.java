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

package dev.razil.folio.itemanimators;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mikepenz on 08.01.16.
 */
public class SlideUpAlphaAnimator extends DefaultAnimator<SlideUpAlphaAnimator> {
    @Override
    public void addAnimationPrepare(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationY(holder.itemView, holder.itemView.getHeight());
        ViewCompat.setAlpha(holder.itemView, 0);
    }

    @Override
    public ViewPropertyAnimatorCompat addAnimation(RecyclerView.ViewHolder holder) {
        return ViewCompat.animate(holder.itemView).translationY(0).alpha(1).setDuration(getMoveDuration()).setInterpolator(getInterpolator());
    }

    @Override
    public void addAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationY(holder.itemView, 0);
        ViewCompat.setAlpha(holder.itemView, 1);
    }

    @Override
    public long getAddDelay(long remove, long move, long change) {
        return 0;
    }

    @Override
    public long getRemoveDelay(long remove, long move, long change) {
        return 0;
    }

    @Override
    public ViewPropertyAnimatorCompat removeAnimation(RecyclerView.ViewHolder holder) {
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(holder.itemView);
        return animation.setDuration(getRemoveDuration()).alpha(0).translationY(holder.itemView.getHeight()).setInterpolator(getInterpolator());
    }

    @Override
    public void removeAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationY(holder.itemView, 0);
        ViewCompat.setAlpha(holder.itemView, 1);
    }
}
