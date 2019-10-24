package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.RichViewHolder;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.parser.HttpParser;

import ink.itwo.common.img.IMGLoad;

/**
 Created by pangw on 2018/6/26. */

public class RichTxChatBox extends BaseChatRow {
    public RichTxChatBox(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, final FromToMessage detail, int position) {
        RichViewHolder holder = (RichViewHolder) baseHolder;
        final FromToMessage message = detail;
        if (message != null) {
            final CardInfo ci = HttpParser.getCardInfo(message.cardInfo, 0);
            holder.getWithdrawTextView().setVisibility(View.GONE);
            holder.getContainer().setVisibility(View.VISIBLE);

            holder.getTitle().setText(ci.title);
            holder.getContent().setText(ci.concent);
            holder.getName().setText(ci.name);

            if (ci.icon.equals("")) {
                holder.getImageView().setVisibility(View.GONE);
            } else {
                holder.getImageView().setVisibility(View.VISIBLE);
            }

//                Glide.with(context).load(ci.icon)
//                        .centerCrop()
//                        .crossFade()
//                        .placeholder(R.drawable.kf_pic_thumb_bg)
//                        .error(R.drawable.kf_image_download_fail_icon)
//                        .into(holder.getImageView());
            IMGLoad.with(context).load(ci.icon)
                    .centerCrop()
//                    .crossFade()
                    .placeholder(R.drawable.kf_pic_thumb_bg)
                    .error(R.drawable.kf_image_download_fail_icon)
                    .into(holder.getImageView());
            holder.getKf_chat_rich_lin().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(ci.url);
                        intent.setData(content_url);
                        context.startActivity(intent);

                    } catch (Exception e) {

                    }
                }
            });
            View.OnClickListener listener = ((ChatActivity) context).getChatAdapter().getOnClickListener();
            getMsgStateResId(position, holder, message, listener);
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_rich_tx, null);
            RichViewHolder holder = new RichViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.RICHTEXT_ROW_TRANSMIT.ordinal();
    }
}
