package com.lawyer.controller.apply;

import android.os.Bundle;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.entity.LawyerApplyBean;
import com.lawyer.enums.LawyerApplyStateEnum;

/**
 律师入驻
 Created by wangtaian on 2019-05-30. */
public class ApplyFm extends BaseFragment<MainActivity> {
    public static ApplyFm newInstance(LawyerApplyStateEnum state) {

        Bundle args = new Bundle();

        ApplyFm fragment = new ApplyFm();
        args.putSerializable("state", state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_apply;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("律师入驻");
        setSwipeBackEnable(true);
//        loadRootFragment(R.id.frame_layout, new ApplyFirstFm());
        LawyerApplyStateEnum stateEnum = (LawyerApplyStateEnum) getArguments().getSerializable("state");
        if (stateEnum == null) return;
        if (stateEnum == LawyerApplyStateEnum.applied) {
            loadRootFragment(R.id.frame_layout, new ApplyThreeFm());
            return;
        }
        ApplyFirstFm fm = new ApplyFirstFm();
        fm.setCallback(new ApplyFirstFm.ApplyFirstFmCallback() {
            @Override
            public void onResult(LawyerApplyBean bean) {
                getSupportDelegate().startChildWithPop(ApplySecondFm.newInstance(bean));
            }
        });
        loadRootFragment(R.id.frame_layout, fm, true, false);
    }
}
