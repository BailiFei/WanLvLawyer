package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ImageViewLookActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.ImageViewHolder;
import com.moor.imkf.model.entity.FromToMessage;

import ink.itwo.common.img.IMGLoad;

/**
 * Created by longwei on 2016/3/10.
 */
public class ImageRxChatRow extends BaseChatRow {

    public ImageRxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        ImageViewHolder holder = (ImageViewHolder) baseHolder;
        final FromToMessage message = detail;
        if(message != null) {
            if(message.withDrawStatus) {
                holder.getWithdrawTextView().setVisibility(View.VISIBLE);
                holder.getContainer().setVisibility(View.GONE);
            }else {
                holder.getWithdrawTextView().setVisibility(View.GONE);
                holder.getContainer().setVisibility(View.VISIBLE);

                message.message = message.message.replaceAll("https://","http://");
//                Glide.with(context)
                IMGLoad.with(context)
                        .load(message.message+"?imageView2/0/w/200/h/140")
                        .centerCrop()
//                        .fitCenter()
//                        .crossFade()
                        .placeholder(R.drawable.kf_pic_thumb_bg)
                        .error(R.drawable.kf_image_download_fail_icon)
                        .override(200,140)
                        .into(holder.getImageView());

                holder.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageViewLookActivity.class);
                        intent.putExtra("imagePath", message.message);
                        intent.putExtra("fromwho",0);//0代表对方发送的
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_image_rx, null);
            ImageViewHolder holder = new ImageViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.IMAGE_ROW_RECEIVED.ordinal();
    }
}
