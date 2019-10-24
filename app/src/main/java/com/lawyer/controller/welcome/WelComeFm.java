package com.lawyer.controller.welcome;

import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.main.MainFm;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 Created by wangtaian on 2019/4/2. */
public class WelComeFm extends BaseFragment<MainActivity> {
    @Override
    protected int initLayoutId() {
        return R.layout.fm_wel_come;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(false);
        Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposableLife(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mActivity.startWithPop(new MainFm());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
