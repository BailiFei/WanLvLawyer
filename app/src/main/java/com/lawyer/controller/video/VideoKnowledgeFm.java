package com.lawyer.controller.video;

import android.os.Bundle;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.base.WebViewFm;
import com.lawyer.controller.video.vm.VideoKnowledgeViewModel;
import com.lawyer.databinding.FmVideoKnowledgeBinding;

/**
 @author wang
 on 2019/2/12 */

public class VideoKnowledgeFm extends AbsFm<FmVideoKnowledgeBinding, VideoKnowledgeViewModel> {
    public static VideoKnowledgeFm newInstance() {

        Bundle args = new Bundle();

        VideoKnowledgeFm fragment = new VideoKnowledgeFm();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fm_video_knowledge;
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected VideoKnowledgeViewModel initViewModel() {
        return new VideoKnowledgeViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            mActivity.start(WebViewFm.newInstance((String) object,"知识详情"));
        }
    }
}
