package com.lawyer.controller.account;

import android.os.Bundle;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.WebViewFm;
import com.lawyer.controller.account.vm.RegisterViewModel;
import com.lawyer.databinding.FmAccountRegiBinding;
import com.lawyer.mvvm.vm.VMSupportFragment;
import com.lawyer.net.Url;

import ink.itwo.common.widget.CountDownTimerView;

/**
 注册登录
 @author wang
 on 2019/1/30 */

public class RegisterFm extends VMSupportFragment<FmAccountRegiBinding, RegisterViewModel, MainActivity> {
    public static final String signup = "signup", forgetPwd = "forgetPwd";

    public static RegisterFm newInstance(String type) {

        Bundle args = new Bundle();

        RegisterFm fragment = new RegisterFm();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private LoginResultListener resultListener;

    public void setResultListener(LoginResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_account_regi;
    }


    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bind.countDownView.setListener(countDownTimerViewListener);
        String type = getArguments().getString("type");
        putSkip(11, type);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    @Override
    protected RegisterViewModel initViewModel() {
        return new RegisterViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
        //key = 1 注册成功
            if (resultListener != null) resultListener.onLoginResult(key == 1);
            pop();
        }
        if (key == 2) {
            //忘记密码修改密码成功
            pop();

        }
        if (key == 3) {
            mActivity.start(WebViewFm.newInstance(Url.userAgreement,"用户注册协议"));
        } else if (key == 4) {
            mActivity.start(WebViewFm.newInstance(Url.privacyAgreement,"隐私声明"));
        }
    }

    private CountDownTimerView.CountDownTimerViewListener countDownTimerViewListener = new CountDownTimerView.CountDownTimerViewListener() {
        @Override
        public void onStart() {
            vm.getCheckCode();
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    };
}
