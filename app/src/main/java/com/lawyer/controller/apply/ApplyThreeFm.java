package com.lawyer.controller.apply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;

/**
 Created by wangtaian on 2019-05-30. */
public class ApplyThreeFm extends BaseFragment<MainActivity> {
    public static ApplyThreeFm newInstance() {

        Bundle args = new Bundle();

        ApplyThreeFm fragment = new ApplyThreeFm();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fm_apply_three;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        viewRoot.findViewById(R.id.tv_back).setOnClickListener(v -> {
            Fragment fragment = getParentFragment();
            if (fragment instanceof ApplyFm) {
                ((ApplyFm) fragment).pop();
            }
        });
    }
}
