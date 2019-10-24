package com.lawyer.mvvm.adapter.view;

import android.Manifest;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.dialog.RxAlertDialog;
import com.lawyer.util.rxView.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import ink.itwo.common.manager.ActivityManager;
import ink.itwo.common.util.ILog;
import ink.itwo.common.util.IToast;
import ink.itwo.common.widget.CountDownTimerView;
import ink.itwo.net.life.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 Created by wang
 on 2018/5/26 */
@BindingMethods({
        @BindingMethod(type = CountDownTimerView.class,
                attribute = "android:mobile", method = "setMobile"),
        @BindingMethod(type = CountDownTimerView.class,
                attribute = "android:countDownListener", method = "setListener"),

        @BindingMethod(type = android.support.v4.widget.SwipeRefreshLayout.class,
                attribute = "android:refreshing", method = "setRefreshing"),
        @BindingMethod(type = android.support.v4.widget.SwipeRefreshLayout.class,
                attribute = "android:refreshListener", method = "setOnRefreshListener"),

        @BindingMethod(type = ViewPager.class,
                attribute = "android:pageMargin", method = "setPageMargin")
})
public class ViewAdapter {


    @BindingAdapter(value = {"callMobile", "fragmentManager", "rxLifecycleTag"})
    public static void onCall(View view, String mobile, FragmentManager manager, Object rxLifecycleTag) {
        if (TextUtils.isEmpty(mobile)) return;
        RxView
                .clicks(view)
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Object o) throws Exception {
                        return RxAlertDialog.newBuilder()
                                .titleSingleStr("拨打电话" + mobile + "?")
                                .commitCancel(true)
                                .build().observe(manager, mobile);
                    }
                })
                .compose(RxLifecycle.bind(rxLifecycleTag))
                .flatMap((Function<Boolean, ObservableSource<Permission>>) aBoolean -> {
                    if (aBoolean) {
                        return new RxPermissions((FragmentActivity) ActivityManager.getActivity().get())
                                .requestEach(Manifest.permission.CALL_PHONE);
                    }
                    return Observable.never();
                })
                .subscribe(permission -> {
                    if (permission == null) return;
                    if (permission.granted) {
                        // 用户已经同意该权限
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                        ActivityManager.getActivity().get().startActivity(intent);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        ILog.d(permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        IToast.show("拨打电话需要您授予权限，请在系统设置里开启电话权限");
                        ILog.d(permission.name + " is denied.");
                    }
                }).isDisposed();


    }

    public static Observable<Boolean> onCall(String mobile, FragmentManager manager, Object rxLifecycleTag) {
        if (TextUtils.isEmpty(mobile)) return Observable.empty();
        return Observable.just(1)
                .flatMap((Function<Object, ObservableSource<Boolean>>) o ->
                        RxAlertDialog.newBuilder()
                                .titleSingleStr("拨打电话" + mobile + "?")
                                .commitCancel(true)
                                .build().observe(manager, mobile)
                                .compose(RxLifecycle.bind(rxLifecycleTag))
                                .flatMap((Function<Boolean, ObservableSource<Permission>>) aBoolean -> {
                                    if (aBoolean) {
                                        return new RxPermissions((FragmentActivity) ActivityManager.getActivity().get())
                                                .requestEach(Manifest.permission.CALL_PHONE);
                                    }
                                    return Observable.never();
                                })
                                .flatMap((Function<Permission, ObservableSource<Boolean>>) permission -> {
                                    if (permission == null) return Observable.never();
                                    if (permission.granted) {
                                        // 用户已经同意该权限
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                                        ActivityManager.getActivity().get().startActivity(intent);
                                        return Observable.just(true);
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                        return Observable.empty();
                                    } else {
                                        // 用户拒绝了该权限，并且选中『不再询问』
                                        IToast.show("拨打电话需要您授予权限，请在系统设置里开启电话权限");
                                        return Observable.empty();
                                    }
                                }));
    }
}
