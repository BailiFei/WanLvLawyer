package com.lawyer.controller.main;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.video.VideoKnowledgeFm;
import com.lawyer.controller.video.VideoShortFm;

/**
 短剧
 @author wang
 on 2019/2/11 */

public class MainVideoFm extends BaseFragment<MainActivity> implements RadioGroup.OnCheckedChangeListener {
    private BaseFragment[] fragments = new BaseFragment[2];
    private RadioButton rdShort, rdKnowledge;
    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_video;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        ((RadioGroup) viewRoot.findViewById(R.id.radio_group))
                .setOnCheckedChangeListener(this);
        fragments[0] = VideoShortFm.newInstance();
        fragments[1] = VideoKnowledgeFm.newInstance();
        loadMultipleRootFragment(R.id.frame_layout, 0, fragments);
        rdShort = findViewById(R.id.radio_short);
        rdKnowledge = findViewById(R.id.radio_knowledge);
        viewRoot.findViewById(R.id.radio_short).performClick();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        showHideFragment(fragments[checkedId == R.id.radio_short ? 0 : 1]);
        rdShort.setTextSize(checkedId == R.id.radio_short ? 19f : 17f);
        rdKnowledge.setTextSize(checkedId == R.id.radio_short ? 17f : 19f);
    }
}

