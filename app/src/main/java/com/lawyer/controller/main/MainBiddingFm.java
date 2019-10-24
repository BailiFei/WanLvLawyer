package com.lawyer.controller.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.cases.CasesDetailFm;
import com.lawyer.controller.main.vm.BiddingViewModel;
import com.lawyer.databinding.FmMainBiddingBinding;
import com.lawyer.entity.UserCaseBean;

import ink.itwo.common.util.DeviceUtil;

/**
 竞标
 @author wang
 on 2019/2/11 */

public class MainBiddingFm extends AbsFm<FmMainBiddingBinding, BiddingViewModel> {
    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_bidding;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        int with = DeviceUtil.getWith();
        int bannerHeight = with * 200 / 375;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bind.layoutBanner.getLayoutParams();
        params.height = bannerHeight;
        bind.layoutBanner.setLayoutParams(params);

        RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) bind.indicator.getLayoutParams();
        indicatorParams.topMargin = with * 172 / 375;
        bind.indicator.setLayoutParams(indicatorParams);
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected BiddingViewModel initViewModel() {
        return new BiddingViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        super.onSkip(key, object);
        if (key == 1) {
            //跳转到案件详情
            mActivity.start(CasesDetailFm.newInstance((UserCaseBean) object, true));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (vm != null) vm.onRefresh();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//        super.onLazyInitView(savedInstanceState);
    }
}


