package com.lawyer.controller.video;

import android.os.Bundle;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.video.vm.VideoShortViewModel;
import com.lawyer.databinding.FmVideoShortBinding;

/**
 短剧-法律短剧
 @author wang
 on 2019/2/12 */

public class VideoShortFm extends AbsFm<FmVideoShortBinding,VideoShortViewModel> {
    public static VideoShortFm newInstance() {

        Bundle args = new Bundle();

        VideoShortFm fragment = new VideoShortFm();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fm_video_short;
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected VideoShortViewModel initViewModel() {
        return new VideoShortViewModel(this, bind);
    }
}
