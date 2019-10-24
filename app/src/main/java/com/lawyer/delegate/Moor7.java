package com.lawyer.delegate;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.lawyer.MainActivity;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.account.LoginFm;
import com.lawyer.controller.account.LoginResultListener;
import com.lawyer.controller.main.MainFm;
import com.lawyer.controller.payment.PaymentResultFm;
import com.lawyer.dialog.MemberHintDialog;
import com.lawyer.entity.User;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.util.Constant;
import com.lawyer.util.LiveEventBus;
import com.lawyer.util.UserCache;
import com.m7.imkfsdk.KfStartHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import ink.itwo.common.util.CacheUtil;
import ink.itwo.common.util.ILog;
import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 Created by wangtaian on 2019/3/20. */
public class Moor7 {
    private BaseFragment fragment;
    private MainActivity mActivity;

    public Moor7(BaseFragment fragment) {
        this.fragment = fragment;
        mActivity = (MainActivity) fragment.getActivity();
    }

    //在线咨询
    public void online() {
        isVip()
                .flatMap((Function<Boolean, ObservableSource<Permission>>) aBoolean -> {
                    if (!aBoolean) return Observable.empty();
                    return new RxPermissions(fragment.getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                })
                .flatMap((Function<Permission, ObservableSource<Permission>>) permission -> {
                    if (permission == null) {
                        return Observable.empty();
                    } else if (permission.granted) {
                        return new RxPermissions(mActivity).requestEach(Manifest.permission.READ_PHONE_STATE);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                        IToast.show(permission.name + " is denied. More info should be provided.");
                        IToast.show("在线咨询需要您授予权限，请在系统设置里开启读写权限");
                        return Observable.empty();
                    } else {// 用户拒绝了该权限，并且选中『不再询问』
                        IToast.show("在线咨询需要您授予权限，请在系统设置里开启读写权限");
                        return Observable.empty();
                    }
                })
                .flatMap((Function<Permission, ObservableSource<Object>>) permission -> {
                    //在线聊天必须的权限
                    if (permission == null) {
                        return Observable.empty();
                    } else if (permission.granted) {
                        return Observable.just(true);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                        IToast.show(permission.name + " is denied. More info should be provided.");
                        IToast.show("在线咨询需要您授予权限，请在系统设置里开启权限");
                        return Observable.empty();
                    } else {// 用户拒绝了该权限，并且选中『不再询问』
                        IToast.show("在线咨询需要您授予权限，请在系统设置里开启权限");
                        return Observable.empty();
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        fragment.addDisposableLife(d);
                    }

                    @Override
                    public void onNext(Object o) {
                        //调用7moor
                        KfStartHelper helper = new KfStartHelper(mActivity/*, UserCache.get().getCustomerId()*/);
                        helper.initSdkChat(Constant.moor_7_accessId, UserCache.get().getNickName(), String.valueOf(UserCache.get().getId()));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ILog.d(throwable);
                    }

                    @Override
                    public void onComplete() {
                        ILog.d("onComplete");
                    }
                });
    }

    public void onCall() {
        isVip()
                .flatMap((Function<Boolean, ObservableSource<Permission>>) aBoolean -> {
                    if (!aBoolean) return Observable.empty();
                    return new RxPermissions(mActivity).requestEach(Manifest.permission.CALL_PHONE);
                })
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        fragment.addDisposableLife(d);
                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission == null) {
                        } else if (permission.granted) {
                            fragment.popTo(MainFm.class, false, () -> {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + CacheUtil.getString(Constant.cache_key_telNum, "")));
                                mActivity.startActivity(intent);
                            });
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            IToast.show(permission.name + " is denied. More info should be provided.");
                            IToast.show("电话咨询需要您授予权限，请在系统设置里开启读写权限");
                        } else {// 用户拒绝了该权限，并且选中『不再询问』
                            IToast.show("电话咨询需要您授予权限，请在系统设置里开启读写权限");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Boolean> isVip() {
        return Observable.just(UserCache.isLogged())
                .flatMap((Function<Boolean, ObservableSource<Boolean>>) aBoolean -> {
                    //是否登录，未登录前往登录
                    if (aBoolean) return Observable.just(true);
                    return Observable.create(emitter -> {
                        LoginFm loginFm = LoginFm.newInstance();
                        LoginResultListener resultListener = isSuccess -> {
                            if (isSuccess) {
                                emitter.onNext(true);
                            } else {
                                emitter.onComplete();
                            }
                        };
                        loginFm.setResultListener(resultListener);
                        mActivity.start(loginFm);
                    });
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<Result<User>>>() {
                    @Override
                    public ObservableSource<Result<User>> apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) return Observable.empty();
                        return NetManager.http().create(API.class)
                                .userQuery(UserCache.getAccessToken());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Result<User>, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Result<User> result) throws Exception {
                        User user = result.getData();
                        if (user == null) {
                            return Observable.empty();
                        }

                        User userInCache = UserCache.get();
                        userInCache.setCustomerId(user.getCustomerId());
                        UserCache.put(userInCache);

                        if (user.getIsVip() == 0) {
                            MemberHintDialog.newInstance().show(mActivity.getSupportFragmentManager(), "MemberHintDialog");
                            return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                    LiveEventBus.get()
                                            .with(PaymentResultFm.BUS_PAYMENT, Boolean.class)
                                            .observe(fragment, new android.arch.lifecycle.Observer<Boolean>() {
                                                @Override
                                                public void onChanged(@Nullable Boolean value) {
                                                    emitter.onNext(value);
                                                }
                                            });
                                }
                            });
                        } else {
                            //已经是vip，继续
                            return Observable.just(true);
                        }

                    }
                });

    }
}
