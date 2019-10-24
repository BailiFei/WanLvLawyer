package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2016/3/10.
 */
public class FileViewHolder extends BaseHolder {

    private TextView chat_content_tv_name;
    private TextView chat_content_tv_size;
    private TextView chat_content_tv_status;
    private ProgressBar chat_content_pb_progress;
    private ImageView chat_content_iv_download;

    public FileViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        chat_content_tv_name = (TextView) baseView.findViewById(R.id.chat_content_tv_name);
        chat_content_tv_size = (TextView) baseView.findViewById(R.id.chat_content_tv_size);
        chat_content_tv_status = (TextView) baseView.findViewById(R.id.chat_content_tv_status);
        chat_content_pb_progress = (ProgressBar) baseView.findViewById(R.id.chat_content_pb_progress);
        if(isReceive) {
            chat_content_iv_download = (ImageView) baseView.findViewById(R.id.chat_content_iv_download);
            type = 8;
            return this;
        }
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 9;
        return this;
    }

    public TextView getChat_content_tv_name() {
        if(chat_content_tv_name == null) {
            chat_content_tv_name = (TextView) getBaseView().findViewById(R.id.chat_content_tv_name);
        }
        return chat_content_tv_name;
    }
    public TextView getChat_content_tv_size() {
        if(chat_content_tv_size == null) {
            chat_content_tv_size = (TextView) getBaseView().findViewById(R.id.chat_content_tv_size);
        }
        return chat_content_tv_size;
    }
    public TextView getChat_content_tv_status() {
        if(chat_content_tv_status == null) {
            chat_content_tv_status = (TextView) getBaseView().findViewById(R.id.chat_content_tv_status);
        }
        return chat_content_tv_status;
    }
    public ProgressBar getChat_content_pb_progress() {
        if(chat_content_pb_progress == null) {
            chat_content_pb_progress = (ProgressBar) getBaseView().findViewById(R.id.chat_content_pb_progress);
        }
        return chat_content_pb_progress;
    }
    public ImageView getChat_content_iv_download() {
        if(chat_content_iv_download == null) {
            chat_content_iv_download = (ImageView) getBaseView().findViewById(R.id.chat_content_iv_download);
        }
        return chat_content_iv_download;
    }
}
