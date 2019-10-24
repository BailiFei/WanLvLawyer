package com.lawyer;

import android.os.Bundle;

import com.lawyer.base.BaseActivity;
import com.lawyer.controller.welcome.SplashFm;
import com.lawyer.controller.welcome.WelComeFm;

import ink.itwo.common.util.CacheUtil;


/**
 @author @wang
 Created at 2019/1/30  16:01 */

public class MainActivity extends BaseActivity {
    public static final String KEY_FIRST = "KEY_FIRST_190719";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSwipeBackEnable(false);
        if (CacheUtil.is(KEY_FIRST, true)) {
            CacheUtil.put(KEY_FIRST, false);
            loadRootFragment(R.id.frame_layout, new SplashFm());
        } else {
            loadRootFragment(R.id.frame_layout, new WelComeFm());
        }

        //7陌 3.2.3版本去掉专属客服功能
    }

}
