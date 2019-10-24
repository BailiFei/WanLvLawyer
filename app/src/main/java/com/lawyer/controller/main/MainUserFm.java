package com.lawyer.controller.main;

import android.view.View;
import android.widget.TextView;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.account.MessageFm;
import com.lawyer.controller.account.SettingFm;
import com.lawyer.controller.apply.ApplyFailFm;
import com.lawyer.controller.apply.ApplyFm;
import com.lawyer.controller.cases.CasesEntrustFm;
import com.lawyer.controller.main.vm.UserViewModel;
import com.lawyer.controller.payment.TopUpFm;
import com.lawyer.controller.payment.WithdrawFm;
import com.lawyer.databinding.FmMainUserBinding;
import com.lawyer.delegate.Moor7;
import com.lawyer.entity.LawyerApplyBean;
import com.lawyer.entity.User;
import com.lawyer.enums.LawyerApplyStateEnum;
import com.lawyer.mvvm.adapter.view.ViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.Constant;
import com.lawyer.util.UserCache;
import com.lawyer.util.rxView.RxView;
import com.lawyer.view.DisplayImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ink.itwo.alioss.upIv.OSSImageViewListener;
import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CacheUtil;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.ILog;
import ink.itwo.media.bean.MediaBean;
import ink.itwo.media.config.Config;
import ink.itwo.media.config.MimeType;
import ink.itwo.media.rx.RxMediaLoad;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 我的
 @author wang
 on 2019/2/11 */

public class MainUserFm extends AbsFm<FmMainUserBinding, UserViewModel> {
    private Moor7 moor7;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_user;
    }


    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected UserViewModel initViewModel() {
        return new UserViewModel(this, bind);
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        TextView tvServiceNum = findViewById(R.id.tv_service_num);
        String mobile = CacheUtil.getString(Constant.cache_key_telNum, "");
        tvServiceNum.setText(mobile);
        RxView.clicks(findViewById(R.id.layout_mobile))
                .flatMap((Function<Object, ObservableSource<?>>) o ->
                        ViewAdapter.onCall(mobile, mActivity.getSupportFragmentManager(),
                                MainUserFm.this))
                .subscribe();
        findViewById(R.id.iv_message)
                .setOnClickListener(v -> mActivity.start(MessageFm.newInstance()));
        findViewById(R.id.tv_online).setOnClickListener(v -> {
            if (moor7 == null) {
                moor7 = new Moor7(MainUserFm.this);
            }
            moor7.online();
        });
        findViewById(R.id.tv_call).setOnClickListener(v -> {
            if (moor7 == null) {
                moor7 = new Moor7(MainUserFm.this);
            }
            moor7.onCall();
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (vm != null) vm.getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (moor7 != null) {
            moor7 = null;
        }
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            mActivity.start(CasesEntrustFm.newInstance());
        } else if (key == 2) {
            mActivity.start(TopUpFm.newInstance());
        } else if (key == 3) {
            //提现暂不支持
            User user = (User) object;
            mActivity.start(WithdrawFm.newInstance(user == null ? null : user.getMoney()));
        } else if (key == 4) {
            mActivity.start(SettingFm.newInstance());
        } else if (key == 5) {
            //跳转到视频模块
            MainFm fragment = mActivity.findFragment(MainFm.class);
            if (fragment != null) {
                fragment.onPerformClick(MainFm.video);
            }
        } else if (key == 6) {
            //跳转到公益模块(1.0.5.2后版本删除公益版本)
//            MainFm fragment = mActivity.findFragment(MainFm.class);
//            if (fragment != null) {
//                fragment.onPerformClick(MainFm.welfare);
//            }
        } else if (key == 7) {
            //修改头像
            new RxMediaLoad(this)
                    .pick(new Config().setCamera(true)
                            .setCircleCrop(false)
//                            .setCrop(true)
                            .setSaveRectangle(true)
                            .setMimeType(MimeType.PHOTO)
                            .setSingle(true)
                            .setImageLoader(new DisplayImageUtil()))
                    .execute()
                    .flatMap((Function<ArrayList<MediaBean>, ObservableSource<String>>) mediaBeans -> {
                        if (CollectionUtil.isEmpty(mediaBeans)) return Observable.empty();
                        IMGLoad.with(MainUserFm.this).load(mediaBeans.get(0).getPath()).into(bind.ivHead);
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
                                vm.getData();
                            }
//                            User user = UserCache.get();
//                            user.setAvatarUrl(result.getData().getAvatarUrl());
//                            UserCache.put(user);

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
        } else if (key == 8) {
            //律师申请入驻
            NetManager.http().create(API.class)
                    .lawyerApplyDetail(UserCache.getAccessToken())
                    .compose(NetTransformer.handle(this))
                    .subscribe(new ResultObserver<Result<LawyerApplyBean>>() {
                        @Override
                        public void onNext(Result<LawyerApplyBean> result) {
                            LawyerApplyBean data = result.getData();
                            if (data == null) {
                                mActivity.start(ApplyFm.newInstance(LawyerApplyStateEnum.normal));
                                return;
                            }
                            if (data.getState()== LawyerApplyStateEnum.refused) {
                                //拒绝
                                mActivity.start(ApplyFailFm.newInstance());
                                return;
                            }
                            mActivity.start(ApplyFm.newInstance(data.getState()));
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mActivity.start(ApplyFm.newInstance(LawyerApplyStateEnum.normal));
                        }
                    });

        }
    }
}

