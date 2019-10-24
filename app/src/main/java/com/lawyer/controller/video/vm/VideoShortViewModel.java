package com.lawyer.controller.video.vm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.video.VideoShortChildFm;
import com.lawyer.controller.video.VideoShortFm;
import com.lawyer.databinding.FmVideoShortBinding;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.life.RxLifecycle;
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/2/25 */

public class VideoShortViewModel extends AbsViewModel<VideoShortFm, FmVideoShortBinding> {
    private List<CaseTypeBean> titles = new ArrayList<>();
    private FragmentStatePagerAdapter adapter;

    public VideoShortViewModel(VideoShortFm videoShortFm, FmVideoShortBinding fmVideoShortBinding) {
        super(videoShortFm, fmVideoShortBinding);

        adapter = new FragmentStatePagerAdapter(getView().getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return VideoShortChildFm.newInstance(titles.get(position));
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position).getName();
            }
        };
        bind.viewPager.setAdapter(adapter);
        bind.tabLayout.setupWithViewPager(bind.viewPager);
    }

    @Override
    public void getInfo() {
        NetManager.http().create(API.class)
                .caseVideoCaseType(/*UserCache.getAccessToken(),*/0)
                .compose(RxLifecycle.bind(getView()))
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Result<List<CaseTypeBean>>>() {
                    @Override
                    public void onNext(Result<List<CaseTypeBean>> result) {
                        List<CaseTypeBean> list = result.getData();
                        if (!CollectionUtil.isEmpty(list)) {
                            titles.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
