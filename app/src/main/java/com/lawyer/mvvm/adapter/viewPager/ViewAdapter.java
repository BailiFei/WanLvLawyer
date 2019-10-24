package com.lawyer.mvvm.adapter.viewPager;

import android.databinding.BindingAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

import ink.itwo.common.widget.imgRes.ImageResource;
import ink.itwo.common.widget.vp.BannerViewPager;
import ink.itwo.common.widget.vp.OnPageSelectedListener;

/**
 * Created by wang
 * on 2018/6/8
 */
public class ViewAdapter {

    @BindingAdapter(value = {"fragments", "fragmentManager"})
    public static void setFragments(ViewPager viewPager, final List<? extends Fragment> data, FragmentManager manager) {
        if (manager == null || data == null) return;
        FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(manager) {

            @Override
            public int getCount() {
                return data == null ? 0 : data.size();
            }

            @Override
            public Fragment getItem(int position) {
                return data.get(position);
            }
        };
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(data==null ? 0 : data.size());
    }

    @BindingAdapter(value = {"datas","indicatorId","pagerClickListener"},requireAll = false)
    public  static <T extends ImageResource>void setImages(BannerViewPager<T> viewPager, List<T> data,int indicatorId,BannerViewPager.OnViewPagerClickListener pagerClickListener) {
        if (indicatorId != 0) {
            ViewParent parent = viewPager.getParent();
            if (parent instanceof ViewGroup) {
                View indicatorView = ((ViewGroup) parent).findViewById(indicatorId);
                if (indicatorView instanceof OnPageSelectedListener) {
                    viewPager.addOnPageSelectedListener((OnPageSelectedListener)indicatorView);
                }
            }
        }
        if (pagerClickListener != null) {
            viewPager.setOnViewPagerClickListner(pagerClickListener);
        }
        viewPager.setData(data, 0);
    }
}
