package com.lawyer.controller.main;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.base.WebViewFm;
import com.lawyer.controller.cases.CaseTypeFm;
import com.lawyer.controller.main.vm.AdvisoryViewModel;
import com.lawyer.controller.video.VideoFm;
import com.lawyer.databinding.FmMainAdvisoryBinding;
import com.lawyer.delegate.Moor7;
import com.lawyer.entity.CaseExampleBean;
import com.lawyer.entity.CaseKnowledgeBean;
import com.lawyer.entity.VideoBean;
import com.lawyer.net.Url;

import ink.itwo.common.util.DeviceUtil;

/**
 咨询 fm
 @author wang
 on 2019/2/11 */

public class MainAdvisoryFm extends AbsFm<FmMainAdvisoryBinding, AdvisoryViewModel> {
    private Moor7 moor7;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_advisory;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(false);
        int with = DeviceUtil.getWith();
        int bannerHeight = with * 200 / 375;
        FrameLayout.LayoutParams bannerParams = new FrameLayout.LayoutParams(with, bannerHeight);
        bind.layoutBanner.setLayoutParams(bannerParams);

        int indicatorTop = with * 172 / 375;
        RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) bind.indicator.getLayoutParams();
        indicatorParams.topMargin = indicatorTop;
        bind.indicator.setLayoutParams(indicatorParams);

        int layoutVIPTop = with * 188 / 375;
        FrameLayout.LayoutParams layoutVIPParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutVIPParams.topMargin = layoutVIPTop;
        bind.layoutVip.setLayoutParams(layoutVIPParams);

        int gridTop = with * 360 / 375;
        FrameLayout.LayoutParams gridParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gridParams.topMargin = gridTop;
        bind.gridViewCaseType.setLayoutParams(gridParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (moor7 != null) {
            moor7 = null;
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected AdvisoryViewModel initViewModel() {
        return new AdvisoryViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        //跳转到首页的视频模块
        if (key == 1) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof MainFm) {
                ((MainFm) parentFragment).onPerformClick(MainFm.video);
            }
        }
        //跳转到首页的案例模块
        if (key == 2) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof MainFm) {
                ((MainFm) parentFragment).onPerformClick(MainFm.cases);
            }
        }
        if (key == 3) {
            if (moor7 == null) {
                moor7 = new Moor7(this);
            }
            //在线咨询
            moor7.online();
        }
        if (key == 4) {
            //电话咨询
            if (moor7 == null) {
                moor7 = new Moor7(this);
            }
            moor7.onCall();
        }
        //案件详情 H5
        if (key == 11) {
            CaseExampleBean caseExampleBean = (CaseExampleBean) object;
            mActivity.start(WebViewFm.newInstance(Url.h5CaseExampleDetail(caseExampleBean.getId()), "案例详情"));
        }
        //案例知识H5
        if (key == 12) {
            CaseKnowledgeBean bean = (CaseKnowledgeBean) object;
            mActivity.start(WebViewFm.newInstance(Url.h5CaseKnowledgeDetail(bean.getId()), "知识详情"));
        }
        //案件类型查看更多
        if (key == 13) {
            mActivity.start(CaseTypeFm.newInstance());
        }
        if (key == 15) {
            //案件类型，跳转到7moor
            if (moor7 == null) moor7 = new Moor7(this);
            moor7.online();
        }
        //首页视频播放
        if (key == 14) {
            mActivity.start(VideoFm.newInstance((VideoBean) object));
        }
    }

}
