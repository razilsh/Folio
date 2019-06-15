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

package dev.razil.folio.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.StateSet;
import android.view.View;

public final class RippleEffect {
    public static void addRippleEffect(
            View view, boolean rippleEnabled, int backgroundColor, int rippleColor) {

        if (rippleEnabled) {

            // Create RippleDrawable
            view.setBackground(getPressedColorRippleDrawable(backgroundColor, rippleColor));

            // Customize Ripple color
            RippleDrawable rippleDrawable = (RippleDrawable) view.getBackground();

            int[][] states = new int[][] {new int[] {android.R.attr.state_enabled}};

            int[] colors = new int[] {rippleColor};

            ColorStateList colorStateList = new ColorStateList(states, colors);

            rippleDrawable.setColor(colorStateList);

            view.setBackground(rippleDrawable);

        } else {

            // Ripple Disabled
            view.setBackground(new ColorDrawable(backgroundColor));
        }
    }

    private static StateListDrawable createStateListDrawable(int backgroundColor, int rippleColor) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(
                new int[] {android.R.attr.state_pressed}, createDrawable(rippleColor));
        stateListDrawable.addState(StateSet.WILD_CARD, createDrawable(backgroundColor));
        return stateListDrawable;
    }

    private static Drawable createDrawable(int background) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(background);
        return shapeDrawable;
    }

    private static Drawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(
                getPressedColorSelector(normalColor, pressedColor),
                getColorDrawableFromColor(normalColor),
                null);
    }

    private static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {

        return new ColorStateList(new int[][] {new int[] {}}, new int[] {pressedColor});
    }

    private static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}
