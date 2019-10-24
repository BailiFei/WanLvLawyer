package com.lawyer.controller.account.vm;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.controller.account.RegisterFm;
import com.lawyer.databinding.FmAccountRegiBinding;
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

public class RegisterViewModel extends BaseFmViewModel<RegisterFm, FmAccountRegiBinding> {
    @Bindable
    public ObservableField<String> mobileNo = new ObservableField<>();
    @Bindable
    public ObservableField<String> checkCode = new ObservableField<>();
    @Bindable
    public ObservableField<String> password = new ObservableField<>();
    @Bindable
    public ObservableBoolean signup = new ObservableBoolean();
    public View.OnClickListener clearMobile = v -> {
        mobileNo.set("");
        notifyPropertyChanged(BR.mobileNo);
    };
    public View.OnClickListener clearCheckCode = v -> {
        checkCode.set("");
        notifyPropertyChanged(BR.checkCode);
    };
    public View.OnClickListener clearPassword = v -> {
        password.set("");
        notifyPropertyChanged(BR.password);
    };
    public View.OnClickListener loginClick = v -> verify();
    public View.OnClickListener closeClick = v -> onSkip.put(2, false);
    public View.OnClickListener userAgreementClick=v-> onSkip.put(3, true);
    public View.OnClickListener statementClick=v-> onSkip.put(4, true);

    String type = RegisterFm.signup;

    public RegisterViewModel(RegisterFm regiFm, FmAccountRegiBinding fmAccountRegiBinding) {
        super(regiFm, fmAccountRegiBinding);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (key == 11) {
            type = (String) object;
            signup.set(RegisterFm.signup.equalsIgnoreCase(type));
        }
    }

    private void verify() {
        if (!VerifyUtil.isMobile(mobileNo.get())) {
            IToast.show("请输入手机号");
            return;
        }
        if (!VerifyUtil.isCheckCode(checkCode.get())) {
            IToast.show("请输入验证码");
            return;
        }
        if (!VerifyUtil.isPassword(password.get())) {
            return;
        }
        if (RegisterFm.signup.equalsIgnoreCase(type)) {
            register();
        } else {
            forgetPwd();
        }
    }


    private void register() {
        NetManager.http().create(API.class)
                .register(mobileNo.get(), checkCode.get(), password.get())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<AccountBean>>() {
                    @Override
                    public void onNext(Result<AccountBean> result) {
                        AccountBean accountBean = result.getData();
                        if (accountBean != null) {
                            User user = accountBean.getUser();
                            if (user != null) {
                                UserCache.put(user);
                                onSkip.put(1, true);
                            }
                        }
                    }
                });
    }

    private void forgetPwd() {
        NetManager.http().create(API.class)
                .forgetPwd(mobileNo.get(), checkCode.get(), password.get())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<AccountBean>>() {
                    @Override
                    public void onNext(Result<AccountBean> result) {
                        AccountBean accountBean = result.getData();
                        if (accountBean != null) {
                            User user = accountBean.getUser();
                            if (user != null) {
                                UserCache.put(user);
                            }
                        }
                        IToast.show("密码已重置");
                        onSkip.put(2, true);
                    }
                });
    }

    public void getCheckCode() {
        NetManager.http().create(API.class)
                .checkCode(mobileNo.get(), type)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result>() {
                    @Override
                    public void onNext(Result result) {
                        IToast.show("验证码已发送");
                    }
                });
    }


}
