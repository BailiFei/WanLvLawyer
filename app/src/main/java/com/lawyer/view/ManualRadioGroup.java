package com.lawyer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.lawyer.util.UserCache;

/**
 @author wang
 on 2019/3/11 */

public class ManualRadioGroup extends RadioGroup {
    private float centreX, centreY;
    private float width, height;
    private InterceptListener interceptListener;

    public ManualRadioGroup(Context context) {
        super(context);
    }

    public ManualRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth() / 5;
        height = getHeight();
        centreX = getWidth() * 9 / 10;
        centreY = getHeight() / 2;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if ((Math.abs(ev.getX() - centreX) < width / 2) && !UserCache.isLogged() && Math.abs(ev.getY() - centreY) < height / 2) {
               if (interceptListener!=null) interceptListener.onIntercept(true);
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    public void setInterceptListener(InterceptListener interceptListener) {
        this.interceptListener = interceptListener;
    }

    /** 是否拦截 - 我的 - 点击事件 */
    @FunctionalInterface
    public interface InterceptListener {
        /** true : 拦截 */
        void onIntercept(boolean isIntercept);
    }
}
