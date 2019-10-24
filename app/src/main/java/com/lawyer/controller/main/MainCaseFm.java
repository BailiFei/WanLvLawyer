package com.lawyer.controller.main;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.base.WebViewFm;
import com.lawyer.controller.main.vm.CasesViewModel;
import com.lawyer.databinding.FmMainCaseBinding;

/**
 案例
 @author wang
 on 2019/2/11 */

public  class MainCaseFm extends AbsFm<FmMainCaseBinding, CasesViewModel> {


    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_case;
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected CasesViewModel initViewModel() {
        return new CasesViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            mActivity.start(WebViewFm.newInstance((String)object,"案例详情"));
        }
    }
}
