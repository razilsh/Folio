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

package dev.razil.folio.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import dev.razil.folio.R;

public class IndentedLayout extends LinearLayout {

    private static final int DEFAULT_SPACING_PER_DEPTH_DP = 8;
    private static final int DEFAULT_LINE_WIDTH = 4;

    private int indentationDepth;
    private final int spacePerDepthPx;
    private Paint indentationLinePaint;
    private Path indentationLinePath;

    private int originalPaddingStart;

    public IndentedLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        indentationLinePaint = new Paint();
        originalPaddingStart = getPaddingStart();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.IndentedLayout);
        spacePerDepthPx =
                attributes.getDimensionPixelSize(
                        R.styleable.IndentedLayout_spacePerDepth,
                        dpToPx(DEFAULT_SPACING_PER_DEPTH_DP, context));
        int indentationLineColor =
                attributes.getColor(R.styleable.IndentedLayout_indentationLineColor, Color.LTGRAY);
        int indentationLineWidth =
                attributes.getDimensionPixelSize(
                        R.styleable.IndentedLayout_indentationLineWidth,
                        dpToPx(DEFAULT_LINE_WIDTH, context));
        attributes.recycle();

        // Using a Path so that dashes can be rendered
        indentationLinePath = new Path();
        indentationLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indentationLinePaint.setColor(indentationLineColor);
        indentationLinePaint.setStrokeWidth(indentationLineWidth);
        indentationLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        indentationLinePaint.setPathEffect(
                new DashPathEffect(
                        new float[] {indentationLineWidth * 2, indentationLineWidth * 2}, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        indentationLinePath.reset();
        for (int i = 0; i < indentationDepth; i++) {
            float lineStartX = spacePerDepthPx * (i + 1) + indentationLinePaint.getStrokeWidth();
            indentationLinePath.moveTo(lineStartX, 0);
            indentationLinePath.lineTo(lineStartX, getHeight());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawPath(indentationLinePath, indentationLinePaint);
    }

    public void setIndentationDepth(@IntRange(from = 0, to = 1) int depth) {
        indentationDepth = depth;

        int indentationSpacing =
                (int) (indentationDepth * spacePerDepthPx + indentationLinePaint.getStrokeWidth());
        setPaddingRelative(
                originalPaddingStart + indentationSpacing,
                getPaddingTop(),
                getPaddingEnd(),
                getPaddingBottom());

        invalidate();
    }

    @Px
    public static int dpToPx(float dpValue, Context context) {
        return (int)
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dpValue,
                        context.getResources().getDisplayMetrics());
    }
}
