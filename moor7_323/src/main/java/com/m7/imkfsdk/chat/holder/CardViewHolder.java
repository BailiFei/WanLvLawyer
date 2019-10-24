package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m7.imkfsdk.R;

/**
 * Created by pangw on 2018/6/25.
 */

public class CardViewHolder extends BaseHolder {
    private ImageView icon;
    private TextView title;
    private TextView mame;
    private TextView content;
    private TextView send;
    private RelativeLayout mRelativeLayout;

    public RelativeLayout getRelativeLayout() {
        return mRelativeLayout;
    }

    public TextView getSend() {
        return send;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getMame() {
        return mame;
    }

    public TextView getContent() {
        return content;
    }

    public CardViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);
        icon = (ImageView) baseView.findViewById(R.id.kf_chat_card_icon);
        title = (TextView) baseView.findViewById(R.id.kf_chat_card_title);
        mame = (TextView) baseView.findViewById(R.id.kf_chat_card_name);
        content = (TextView) baseView.findViewById(R.id.kf_chat_card_content);
        send = (TextView) baseView.findViewById(R.id.kf_chat_card_send);
        mRelativeLayout = (RelativeLayout) baseView.findViewById(R.id.kf_chat_card_re);

        //通过baseview找到对应组件
        return this;
    }
}
