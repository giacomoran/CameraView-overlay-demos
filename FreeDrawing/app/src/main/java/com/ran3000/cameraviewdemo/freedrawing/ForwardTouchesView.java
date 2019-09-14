package com.ran3000.cameraviewdemo.freedrawing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ForwardTouchesView extends View {

    @Nullable
    private View forwardTo;

    public ForwardTouchesView(Context context) {
        super(context);
    }

    public ForwardTouchesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ForwardTouchesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setForwardTo(@Nullable View v) {
        this.forwardTo = v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (forwardTo != null) {
            return forwardTo.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}


