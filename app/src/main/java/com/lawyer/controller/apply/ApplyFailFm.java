package com.lawyer.controller.apply;

import android.os.Bundle;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.enums.LawyerApplyStateEnum;

/**
 认证失败
 Created by wangtaian on 2019-05-30. */
public class ApplyFailFm extends BaseFragment<MainActivity> {
    public static ApplyFailFm newInstance() {

        Bundle args = new Bundle();

        ApplyFailFm fragment = new ApplyFailFm();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fm_apply_fail;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("认证失败");
        viewRoot.findViewById(R.id.tv_reset)
                .setOnClickListener(v -> {
                    mActivity.startWithPop(ApplyFm.newInstance(LawyerApplyStateEnum.normal));
                });
        viewRoot.findViewById(R.id.tv_back)
                .setOnClickListener(v -> {
                    pop();
                });
    }
}
