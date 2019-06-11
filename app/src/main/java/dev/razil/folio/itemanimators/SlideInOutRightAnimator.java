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
import android.view.View;

/**
 * Created by mikepenz on 08.01.16.
 * Base for this animator thanks to @gabrielemariotti - https://github.com/gabrielemariotti/RecyclerViewItemAnimators
 */
public class SlideInOutRightAnimator extends DefaultAnimator<SlideInOutRightAnimator> {

    private float mDeltaX;
    private RecyclerView mRecyclerView;

    public SlideInOutRightAnimator(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void addAnimationPrepare(RecyclerView.ViewHolder holder) {
        retrieveItemPosition(holder);
        ViewCompat.setTranslationX(holder.itemView, +mDeltaX);
        ViewCompat.setTranslationZ(holder.itemView, 100);
    }

    @Override
    public ViewPropertyAnimatorCompat addAnimation(RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        return ViewCompat.animate(view).translationX(0).alpha(1).setDuration(getAddDuration());
    }

    @Override
    public void addAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setAlpha(holder.itemView, 1);
        ViewCompat.setTranslationX(holder.itemView, 0);
        ViewCompat.setTranslationZ(holder.itemView, 1);
    }

    @Override
    public ViewPropertyAnimatorCompat removeAnimation(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationZ(holder.itemView, 100);
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(holder.itemView);
        return animation.setDuration(getRemoveDuration()).alpha(0).translationX(+mDeltaX);
    }

    @Override
    public void removeAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, 0);
        ViewCompat.setAlpha(holder.itemView, 1);
        ViewCompat.setTranslationZ(holder.itemView, 1);
    }


    private void retrieveItemPosition(final RecyclerView.ViewHolder holder) {
        mDeltaX = mRecyclerView.getWidth() - mRecyclerView.getLayoutManager().getDecoratedLeft(holder.itemView);
    }

    @Override
    public long getAddDelay(long remove, long move, long change) {
        return 0;
    }

    @Override
    public long getRemoveDelay(long remove, long move, long change) {
        return remove / 2;
    }

}
