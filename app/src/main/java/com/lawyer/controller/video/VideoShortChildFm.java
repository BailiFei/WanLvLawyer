package com.lawyer.controller.video;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.video.vm.VideoChildViewModel;
import com.lawyer.databinding.FmVideoShortChildBinding;
import com.lawyer.entity.CaseTypeBean;

import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;

/**
 @author wang
 on 2019/2/12 */

public class VideoShortChildFm extends AbsFm<FmVideoShortChildBinding,VideoChildViewModel> {
    public static VideoShortChildFm newInstance(CaseTypeBean typeBean) {

        Bundle args = new Bundle();

        VideoShortChildFm fragment = new VideoShortChildFm();
        args.putParcelable("typeBean", typeBean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int initLayoutId() {
        return R.layout.fm_video_short_child;
    }


    @Override
    public void onSupportInvisible() {
        Jzvd.releaseAllVideos();
        super.onSupportInvisible();
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bind.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && jzvd.jzDataSource.containsTheUrl(JZMediaManager.getCurrentUrl())) {
                    Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
                    if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
        putSkip(101,getArguments().getParcelable("typeBean"));
    }


    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected VideoChildViewModel initViewModel() {
        return new VideoChildViewModel(this, bind);
    }
}
