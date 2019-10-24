package com.lawyer.controller.main.vm;

import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainUserFm;
import com.lawyer.controller.payment.PaymentResultFm;
import com.lawyer.databinding.FmMainUserBinding;
import com.lawyer.entity.User;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.LiveEventBus;
import com.lawyer.util.UserCache;

import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/25 */

public class UserViewModel extends AbsViewModel<MainUserFm, FmMainUserBinding> {
    @Bindable public User user;
    public View.OnClickListener entrustClick = v -> onSkip.put(1, true);
    public View.OnClickListener topUpClick = v -> onSkip.put(2, true);
    public View.OnClickListener withdrawClick = v -> onSkip.put(3, user);
    public View.OnClickListener setClick = v -> onSkip.put(4, true);
    public View.OnClickListener knowClick = v -> onSkip.put(5, true);
    public View.OnClickListener welfareClick = v -> onSkip.put(6, true);
    public View.OnClickListener headClick = v -> onSkip.put(7, true);
    public View.OnClickListener applyClick = v -> onSkip.put(8, true);


    public UserViewModel(MainUserFm mainUserFm, FmMainUserBinding fmMainUserBinding) {
        super(mainUserFm, fmMainUserBinding);
        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT, Boolean.class)
                .observe(getView(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean != null && aBoolean) UserViewModel.this.getInfo();
                    }
                });
    }

    public void getData() {
        NetManager.http().create(API.class)
                .userQuery(UserCache.getAccessToken())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<User>>() {
                    @Override
                    public void onNext(Result<User> result) {
                        user = result.getData();
                        notifyPropertyChanged(BR.user);
                    }
                });
    }
}
