package com.lawyer.controller.account.vm;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.account.ChangePasswordFm;
import com.lawyer.databinding.FmChangePasswordBinding;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import java.util.Objects;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/27 */

public class ChangePasswordViewModel extends AbsViewModel<ChangePasswordFm, FmChangePasswordBinding> {
    @Bindable
    public ObservableField<String> oldPass = new ObservableField<>();
    @Bindable
    public ObservableField<String> newPass = new ObservableField<>();
    @Bindable
    public ObservableField<String> againPass = new ObservableField<>();
    public View.OnClickListener commitClick = v -> toCommit();

    public ChangePasswordViewModel(ChangePasswordFm changePasswordFm, FmChangePasswordBinding fmChangePasswordBinding) {
        super(changePasswordFm, fmChangePasswordBinding);
    }

    private void toCommit() {
        if (!VerifyUtil.isPassword(oldPass.get())) {
            return;
        }
        if (!VerifyUtil.isPassword(newPass.get())) {
            return;
        }
        if (!VerifyUtil.isPassword(againPass.get())) {
            return;
        }
        if (Objects.equals(oldPass.get(), newPass.get())) {
            IToast.show("新密码与旧密码相同");
            return;
        }
        if (!Objects.equals(newPass.get(), againPass.get())) {
            IToast.show("两次新密码输入的不一致");
            return;
        }
        toChange();
    }

    private void toChange() {
        NetManager.http().create(API.class)
                .userModifyPassword(UserCache.getAccessToken(), oldPass.get(), againPass.get())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result>() {
                    @Override
                    public void onNext(Result result) {
                        IToast.show("密码已修改");
                        onSkip.put(1, true);
                    }
                });
    }
}
