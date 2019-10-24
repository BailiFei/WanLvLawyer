package com.lawyer.controller.account;

import android.os.Bundle;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.account.vm.ChangePasswordViewModel;
import com.lawyer.databinding.FmChangePasswordBinding;

/** 修改密码
 @author wang
 on 2019/2/20 */

public class ChangePasswordFm extends AbsFm<FmChangePasswordBinding, ChangePasswordViewModel> {
    public static ChangePasswordFm newInstance() {

        Bundle args = new Bundle();

        ChangePasswordFm fragment = new ChangePasswordFm();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fm_change_password;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("修改密码");
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            pop();
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected ChangePasswordViewModel initViewModel() {
        return new ChangePasswordViewModel(this, bind);
    }
}
