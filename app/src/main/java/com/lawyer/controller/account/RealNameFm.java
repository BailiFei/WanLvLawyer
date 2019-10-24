package com.lawyer.controller.account;

import android.os.Bundle;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.account.vm.RealNameViewModel;
import com.lawyer.databinding.AccoutRealNameBinding;

/**
 身份认证
 @author wang
 on 2019/3/1 */

public class RealNameFm extends AbsFm<AccoutRealNameBinding, RealNameViewModel> {
    public static RealNameFm newInstance() {

        Bundle args = new Bundle();

        RealNameFm fragment = new RealNameFm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("身份认证");
    }

    @Override
    protected int initLayoutId() {
        return R.layout.accout_real_name;
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected RealNameViewModel initViewModel() {
        return new RealNameViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            pop();
        }
    }
}
