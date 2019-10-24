package com.lawyer.view;

import android.content.Context;
import android.util.AttributeSet;

import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import cn.jzvd.JzvdStd;
import ink.itwo.common.util.ILog;
import ink.itwo.net.NetManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/** 视频播放
 Created by wangtaian on 2019/3/20. */
public class LawyerJzvdStd extends JzvdStd {
    private Disposable disposable;
    private int id;
    public LawyerJzvdStd(Context context) {
        super(context);
    }

    public LawyerJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUp(int id, String url, String title, int screen) {
        super.setUp(url, title, screen);
        this.id = id;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        if (id != 0) {
            upCount(id);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private void upCount(int id) {
        NetManager.http().create(API.class)
                .caseVideoUpdatePlayCount(id)
                .subscribeOn(Schedulers.io())
                .subscribe(new ResultObserver<Result>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(Result result) {
                        ILog.d(result);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
