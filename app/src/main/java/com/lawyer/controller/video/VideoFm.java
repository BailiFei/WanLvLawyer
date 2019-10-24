package com.lawyer.controller.video;

import android.os.Bundle;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.entity.VideoBean;
import com.lawyer.view.LawyerJzvdStd;

import cn.jzvd.JZUserAction;
import cn.jzvd.Jzvd;
import ink.itwo.common.util.ILog;

/**
 视频播放
 @author wang
 on 2019/3/4 */

public class VideoFm extends BaseFragment<MainActivity> {
    public static VideoFm newInstance(VideoBean videoBean) {

        Bundle args = new Bundle();

        VideoFm fragment = new VideoFm();
        args.putParcelable("videoBean", videoBean);
        fragment.setArguments(args);
        return fragment;
    }

    private VideoBean videoBean;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_video;
    }

    @Override
    public void onSupportInvisible() {
        Jzvd.releaseAllVideos();
        super.onSupportInvisible();
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(true);
        videoBean = getArguments().getParcelable("videoBean");
        LawyerJzvdStd videoPlayer = findViewById(R.id.videoplayer);
        videoPlayer.setUp(videoBean.getId(), videoBean.getVideoUrl(), videoBean.getTitle(), Jzvd.SCREEN_WINDOW_NORMAL);
        Jzvd.setJzUserAction(new JZUserAction() {
            @Override
            public void onEvent(int type, Object url, int screen, Object... objects) {
                ILog.d(1);
            }
        });
        videoPlayer.startVideo();

    }
}
