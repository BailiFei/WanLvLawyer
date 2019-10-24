package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2016/3/9.
 */
public class TextViewHolder extends BaseHolder {

    private TextView tv_content;

    public RelativeLayout chat_rl_robot, chat_rl_robot_result;
    public LinearLayout chat_ll_robot_useless, chat_ll_robot_useful,lin_content;
    public ImageView chat_iv_robot_useless, chat_iv_robot_useful;
    public TextView chat_tv_robot_useless, chat_tv_robot_useful, chat_tv_robot_result;


    public TextViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        if(isReceive) {
            type = 1;
            chat_rl_robot = (RelativeLayout) baseView.findViewById(R.id.chat_rl_robot);
            chat_rl_robot_result = (RelativeLayout) baseView.findViewById(R.id.chat_rl_robot_result);
            chat_ll_robot_useless = (LinearLayout) baseView.findViewById(R.id.chat_ll_robot_useless);
            chat_ll_robot_useful = (LinearLayout) baseView.findViewById(R.id.chat_ll_robot_useful);
            lin_content = (LinearLayout) baseView.findViewById(R.id.chart_content_lin);
            chat_iv_robot_useless = (ImageView) baseView.findViewById(R.id.chat_iv_robot_useless);
            chat_iv_robot_useful = (ImageView) baseView.findViewById(R.id.chat_iv_robot_useful);
            chat_tv_robot_useless = (TextView) baseView.findViewById(R.id.chat_tv_robot_useless);
            chat_tv_robot_useful = (TextView) baseView.findViewById(R.id.chat_tv_robot_useful);
            chat_tv_robot_result = (TextView) baseView.findViewById(R.id.chat_tv_robot_result);
            return this;
        }else {
            //通过baseview找到对应组件
            tv_content = (TextView) baseView.findViewById(R.id.chat_content_tv);
        }
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 2;
        return this;
    }

    public TextView getDescTextView() {
        if(tv_content == null) {
            tv_content = (TextView) getBaseView().findViewById(R.id.chat_content_tv);
        }
        return tv_content;
    }
    public LinearLayout getDescLinearLayout(){
        if(lin_content ==null){
            lin_content = (LinearLayout) baseView.findViewById(R.id.chart_content_lin);
        }
        return lin_content;
    }



}
