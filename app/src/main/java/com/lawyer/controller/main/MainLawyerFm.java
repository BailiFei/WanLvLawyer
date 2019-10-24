package com.lawyer.controller.main;

import android.support.design.widget.ExtendTabLayout;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.account.MessageFm;
import com.lawyer.controller.account.SettingFm;
import com.lawyer.controller.cases.CasesDetailFm;
import com.lawyer.controller.cases.CasesFm;
import com.lawyer.controller.main.vm.LawyerViewModel;
import com.lawyer.controller.payment.WithdrawFm;
import com.lawyer.databinding.FmMainLawyerBinding;
import com.lawyer.entity.User;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.view.DisplayImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ink.itwo.alioss.upIv.OSSImageViewListener;
import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.ILog;
import ink.itwo.media.bean.MediaBean;
import ink.itwo.media.config.Config;
import ink.itwo.media.config.MimeType;
import ink.itwo.media.rx.RxMediaLoad;
import ink.itwo.net.NetManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/2/12 */

public class MainLawyerFm extends AbsFm<FmMainLawyerBinding, LawyerViewModel> {
    private CaseStateEnum[] titles = {CaseStateEnum.tendering, CaseStateEnum.solving, CaseStateEnum.done};

    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_lawyer;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bind.tabLayout.addTab(bind.tabLayout.newTab().setText(titles[0].getLawyerTitles()));
        bind.tabLayout.addTab(bind.tabLayout.newTab().setText(titles[1].getLawyerTitles()));
        bind.tabLayout.addTab(bind.tabLayout.newTab().setText(titles[2].getLawyerTitles()));
        bind.tabLayout.addOnTabSelectedListener(new ExtendTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(ExtendTabLayout.Tab tab) {
                putSkip(11, tab.getPosition());
            }

            @Override
            public void onTabUnselected(ExtendTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(ExtendTabLayout.Tab tab) {

            }
        });
        findViewById(R.id.iv_set).setOnClickListener(v -> {
            mActivity.start(SettingFm.newInstance());
        });
        findViewById(R.id.iv_message)
                .setOnClickListener(v -> mActivity.start(MessageFm.newInstance()));
        bind.ivHead.setOnClickListener(v -> {
            new RxMediaLoad(this)
                    .pick(new Config()
                            .setCamera(true)
                            .setCircleCrop(false)
//                            .setCrop(true)
                            .setSaveRectangle(true)
                            .setMimeType(MimeType.PHOTO)
                            .setSingle(true)
                            .setImageLoader(new DisplayImageUtil()))
                    .execute()
                    .flatMap((Function<ArrayList<MediaBean>, ObservableSource<String>>) mediaBeans -> {
                        if (CollectionUtil.isEmpty(mediaBeans)) return Observable.empty();
                        IMGLoad.with(MainLawyerFm.this).load(mediaBeans.get(0).getPath()).into(bind.ivHead);
                        return Observable.create(emitter -> {
                            OSSImageViewListener listener = new OSSImageViewListener.SimpleOSSImageListener() {
                                @Override
                                public void onSuccess(String fileUrl) {
                                    emitter.onNext(fileUrl);
                                    emitter.onComplete();

                                }
                            };
                            bind.ivHead.setOSSImageViewListener(listener);
                            bind.ivHead.upLoad(mediaBeans.get(0).getPath(), "head");
                        });
                    })
                    .observeOn(Schedulers.io())
                    .flatMap((Function<String, ObservableSource<Result<User>>>) s -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("accessToken", UserCache.getAccessToken());
                        userMap.put("avatarUrl", s);
                        return NetManager.http()
                                .create(API.class)
                                .userUpdateUserInfo(userMap);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResultObserver<Result<User>>() {
                        @Override
                        public void onNext(Result<User> result) {
                            ILog.d(result);
                            if (vm != null) {
                                vm.getUserInfo();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }

                        @Override
                        protected boolean isToastEnable() {
                            return false;
                        }
                    });
        });
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected LawyerViewModel initViewModel() {
        return new LawyerViewModel(this, bind);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (vm != null) vm.getUserInfo();
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            //跳转到案件详情
            if (bind.tabLayout.getSelectedTabPosition() == 0) {
                mActivity.start(CasesDetailFm.newInstance((UserCaseBean) object, true));
            } else {
                mActivity.start(CasesFm.newInstance((UserCaseBean) object));
            }
        } else if (key == 3) {
            //提现暂不支持
            User user = (User) object;
            mActivity.start(WithdrawFm.newInstance(user == null ? null : user.getMoney()));
        }

    }
}
