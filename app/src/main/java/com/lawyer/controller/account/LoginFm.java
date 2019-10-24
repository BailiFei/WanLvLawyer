package com.lawyer.controller.account;

import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lawyer.BR;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.controller.account.vm.LoginViewModel;
import com.lawyer.controller.main.MainFm;
import com.lawyer.databinding.FmAccountLoginBinding;
import com.lawyer.enums.AccountTypeEnum;
import com.lawyer.mvvm.vm.VMSupportFragment;
import com.lawyer.util.UserCache;

/**
 登录界面
 @author wang
 on 2019/2/20 */

public class LoginFm extends VMSupportFragment<FmAccountLoginBinding, LoginViewModel, MainActivity>
        implements CheckBox.OnCheckedChangeListener {
    public static LoginFm newInstance() {

        Bundle args = new Bundle();

        LoginFm fragment = new LoginFm();
        fragment.setArguments(args);
        return fragment;
    }

    private LoginResultListener resultListener;

    public void setResultListener(LoginResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_account_login;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bind.checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            //登录成功
            if (resultListener != null) {
                //律师登录
                if (UserCache.get().getAccountType() == AccountTypeEnum.lawyer) {
                    mActivity.startWithPopTo(MainFm.newInstance(), MainFm.class, true);
                    return;
                }
                pop();
                resultListener.onLoginResult(true);
            }
            //服务端踢掉
            else {
                mActivity.startWithPop(MainFm.newInstance());
            }
        } else if (key == 2) {
            //跳到注册
            RegisterFm registerFm = RegisterFm.newInstance(RegisterFm.signup);
            registerFm.setResultListener(isSuccess -> {
                if (isSuccess) resultListener.onLoginResult(true);
            });
            mActivity.start(registerFm);
        } else if (key == 3) {
            //忘记密码
            RegisterFm registerFm = RegisterFm.newInstance(RegisterFm.forgetPwd);
            registerFm.setResultListener(isSuccess -> {
                if (isSuccess) resultListener.onLoginResult(true);
            });
            mActivity.start(registerFm);
        } else if (key == 4) {
            //关闭登录
            if (resultListener != null) resultListener.onLoginResult(false);
            pop();
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected LoginViewModel initViewModel() {
        return new LoginViewModel(this, bind);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        bind.edit.setTransformationMethod(isChecked ?
                PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
        bind.edit.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = bind.edit.getText();
        if (charSequence != null) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
