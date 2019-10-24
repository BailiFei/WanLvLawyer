package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2016/3/10.
 */
public class ImageViewHolder extends BaseHolder {

    private ImageView iv_content;

    public ImageViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        iv_content = (ImageView) baseView.findViewById(R.id.chat_content_iv);
        if(isReceive) {
            type = 3;
            return this;
        }
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 4;
        return this;
    }

    public ImageView getImageView() {
        if(iv_content == null) {
            iv_content = (ImageView) getBaseView().findViewById(R.id.chat_content_iv);
        }
        return iv_content;
    }
}
