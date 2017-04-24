package com.jjkbashlord.slidingtextview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by JJK on 4/24/17.
 */

public class ResizeAnimation  extends Animation {

    private int startWidth;
    private int deltaWidth; // distance between start and end width
    private View view;

    /**
     * constructor, do not forget to use the setParams(int, int) method before
     * starting the animation
     * @param v
     */
    public ResizeAnimation (View v) {
        this.view = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //view.setr
        view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);
        view.requestLayout();
    }

    /**
     * set the starting and ending width for the resize animation
     * starting width is usually the views current width, the end width is the width
     * we want to reach after the animation is completed
     * @param start width in pixels
     * @param end width in pixels
     */
    public void setParams(int start, int end) {

        this.startWidth = start;
        deltaWidth = end - startWidth;
    }

    /**
     * set the duration for the hideshowanimation
     */
    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
