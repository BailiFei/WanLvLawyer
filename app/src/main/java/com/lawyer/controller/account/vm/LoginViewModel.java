package com.lawyer.controller.account.vm;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.lawyer.controller.account.LoginFm;
import com.lawyer.databinding.FmAccountLoginBinding;
import com.lawyer.entity.AccountBean;
import com.lawyer.entity.User;
import com.lawyer.mvvm.vm.BaseFmViewModel;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/21 */

public class LoginViewModel extends BaseFmViewModel<LoginFm, FmAccountLoginBinding> {
    @Bindable
    public ObservableField<String> mobileNo = new ObservableField<>();
    @Bindable
    public ObservableField<String> password = new ObservableField<>();

    public View.OnClickListener loginClick = v -> verify();
    public View.OnClickListener registerClick=v -> onSkip.put(2,true);
    public View.OnClickListener forgetClick=v -> onSkip.put(3,true);
    public View.OnClickListener mobileClearClick=v -> mobileNo.set("");
    public View.OnClickListener passwordClearClick=v -> password.set("");
    //关闭
    public View.OnClickListener closeClick = v -> onSkip.put(4, true);

    public LoginViewModel(LoginFm loginFm, FmAccountLoginBinding fmAccountLoginBinding) {
        super(loginFm, fmAccountLoginBinding);
    }

    private void verify() {
        if (!VerifyUtil.isMobile(mobileNo.get())) {
            IToast.show("请输入手机号");
            return;
        }
        if (!VerifyUtil.isPassword(password.get())) {
            return;
        }
        login();
    }

    private void login() {
        NetManager.http().create(API.class)
                .login(mobileNo.get(), password.get())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<AccountBean>>() {
                    @Override
                    public void onNext(Result<AccountBean> result) {
                        AccountBean accountBean = result.getData();
                        if (accountBean != null) {
                            User user = accountBean.getUser();
                            if (user != null) {
                                UserCache.put(user);
                                onSkip.put(1,true);
                            }
                        }
                    }
                });
    }
}
