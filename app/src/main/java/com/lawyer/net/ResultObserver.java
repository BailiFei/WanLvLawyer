package com.lawyer.net;

import android.app.Activity;

import com.lawyer.base.BaseActivity;
import com.lawyer.controller.account.LoginFm;
import com.lawyer.controller.main.MainFm;
import com.lawyer.util.UserCache;

import ink.itwo.common.manager.ActivityManager;
import ink.itwo.common.util.IToast;
import ink.itwo.net.exception.BaseException;
import ink.itwo.net.observer.BaseObserver;

/**
 @author wang
 on 2019/2/21 */

public abstract class ResultObserver<T> extends BaseObserver<T> {
    @Override
    public void onError(Throwable throwable) {
        if (isToastEnable()) {
            IToast.show(throwable.getMessage());
        }
        if (throwable instanceof BaseException) {
            BaseException exception = (BaseException) throwable;
            if (exception.getRetCode() == Code.NO_PERMITTION) {
                Activity activity = ActivityManager.getActivity().get();
                if (activity instanceof BaseActivity) {
                    UserCache.clear();
//                    ((BaseActivity) activity).start(LoginFm.newInstance());
                    //被服务端踢掉的，关闭全部界面并返回登录界面
                    ((BaseActivity) activity).startWithPopTo(LoginFm.newInstance(), MainFm.class, true);
                }
            }
        }
        onComplete();
    }
    protected boolean isToastEnable() {
        return true;
    }
}
