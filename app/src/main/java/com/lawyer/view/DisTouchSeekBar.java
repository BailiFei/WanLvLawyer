package com.lawyer.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 Created by wangtaian on 2019-05-10. */
public class DisTouchSeekBar extends AppCompatSeekBar {

    public DisTouchSeekBar(Context context) {
        super(context);
    }

    public DisTouchSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisTouchSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
