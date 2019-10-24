package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.MoorWebCenter;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.RichViewHolder;
import com.moor.imkf.model.entity.FromToMessage;

import ink.itwo.common.img.IMGLoad;

/**
 * Created by pangw on 2018/3/8.
 */

public class RichRxChatBow extends BaseChatRow {
    public RichRxChatBow(int type) {
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
        if(message != null) {
            if(message.withDrawStatus) {
                holder.getWithdrawTextView().setVisibility(View.VISIBLE);
                holder.getContainer().setVisibility(View.GONE);
            }else {
                holder.getWithdrawTextView().setVisibility(View.GONE);
                holder.getContainer().setVisibility(View.VISIBLE);

                holder.getTitle().setText(message.richTextTitle);
                holder.getTitle().getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                holder.getContent().setText(message.richTextDescription);

                if(message.richTextPicUrl.equals("")){
                    holder.getImageView().setVisibility(View.GONE);
                }else{
                    holder.getImageView().setVisibility(View.VISIBLE);
                }

//                Glide.with(context)
                IMGLoad.with(context)
                        .load(message.richTextPicUrl+"?imageView2/0/w/200/h/140")
                        .centerCrop()
//                        .crossFade()
                        .placeholder(R.drawable.kf_pic_thumb_bg)
                        .error(R.drawable.kf_image_download_fail_icon)
                        .into(holder.getImageView());
                holder.getKf_chat_rich_lin().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent forumIntent = new Intent(context, MoorWebCenter.class);
                        forumIntent.putExtra("OpenUrl", message.richTextUrl);
                        forumIntent.putExtra("titleName", message.richTextTitle);
                        context.startActivity(forumIntent);
                    }
                });

            }

        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_rich_rx, null);
            RichViewHolder holder = new RichViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.RICHTEXT_ROW_RECEIVED.ordinal();
    }
}
