package com.lawyer.controller.welcome;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.main.MainFm;

import ink.itwo.common.img.IMGLoad;

/**
 Created by wangtaian on 2019/4/11. */
public class SplashFm extends BaseFragment<MainActivity> {

    @Override
    protected int initLayoutId() {
        return R.layout.fm_splash;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        ViewPager viewPager = findViewById(R.id.view_pager);
        int[] res = {R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4};
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return res.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                IMGLoad.with(SplashFm.this).load(res[position]).into(imageView);
                if (position == 3) {
                    imageView.setOnClickListener(v -> mActivity.startWithPop(new MainFm()));
                }
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }
}
