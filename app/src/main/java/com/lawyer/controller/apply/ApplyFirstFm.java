package com.lawyer.controller.apply;

import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.databinding.FmApplyFirstBinding;
import com.lawyer.entity.LawyerApplyBean;

/**
 Created by wangtaian on 2019-05-30. */
public class ApplyFirstFm extends AbsFm<FmApplyFirstBinding, FirstViewModel> {

    public interface ApplyFirstFmCallback {
        void onResult(LawyerApplyBean bean);
    }

    private ApplyFirstFmCallback callback;

    public ApplyFirstFm setCallback(ApplyFirstFmCallback callback) {
        this.callback = callback;
        return this;
    }
    public void onResult(LawyerApplyBean bean) {
        if (callback != null) {
            callback.onResult(bean);
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_apply_first;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);

    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected FirstViewModel initViewModel() {
        return new FirstViewModel(this, bind);
    }


}
