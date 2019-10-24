package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.m7.imkfsdk.R;

/**
 * Created by pangw on 2018/3/8.
 */

public class RichViewHolder extends BaseHolder {
    private TextView title;
    private TextView content;
    private TextView name;
    private ImageView iv;
    private LinearLayout kf_chat_rich_lin;

    public RichViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        title = (TextView) baseView.findViewById(R.id.kf_chat_rich_title);
        content = (TextView) baseView.findViewById(R.id.kf_chat_rich_content);
        name = (TextView) baseView.findViewById(R.id.kf_chat_rich_name);
        iv = (ImageView) baseView.findViewById(R.id.kf_chat_rich_iv);
        kf_chat_rich_lin = (LinearLayout) baseView.findViewById(R.id.kf_chat_rich_lin);
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);

        return this;
    }

    public ImageView getImageView() {
        if (iv == null) {
            iv = (ImageView) getBaseView().findViewById(R.id.kf_chat_rich_iv);
        }
        return iv;
    }
    public TextView getTitle(){
        if(title == null){
            title = (TextView) baseView.findViewById(R.id.kf_chat_rich_title);
        }
        return title;
    }
    public TextView getContent(){
        if(content == null){
            content = (TextView) baseView.findViewById(R.id.kf_chat_rich_content);
        }
        return content;
    }
    public TextView getName(){
        if(name == null){
            name = (TextView) baseView.findViewById(R.id.kf_chat_rich_name);
        }
        return name;
    }

    public LinearLayout getKf_chat_rich_lin(){
        if(kf_chat_rich_lin==null){
            kf_chat_rich_lin = (LinearLayout) baseView.findViewById(R.id.kf_chat_rich_lin);
        }
        return kf_chat_rich_lin;
    }
}
