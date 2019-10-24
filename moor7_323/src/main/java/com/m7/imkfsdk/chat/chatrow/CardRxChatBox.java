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
import com.m7.imkfsdk.chat.holder.CardViewHolder;
import com.m7.imkfsdk.chat.holder.ViewHolderTag;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.parser.HttpParser;

import ink.itwo.common.img.IMGLoad;

/**
 * Created by pangw on 2018/6/25.
 */

public class CardRxChatBox extends BaseChatRow {
    public CardRxChatBox(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, final FromToMessage detail, int position) {
        CardViewHolder holder = (CardViewHolder) baseHolder;
        final FromToMessage message = detail;
        if(message != null) {

            final CardInfo cardInfo = HttpParser.getCardInfo(detail.cardInfo,0);

//            Glide.with(context)
            IMGLoad.with(context)
                    .load(cardInfo.icon)
                    .centerCrop()
//                    .crossFade()
                    .placeholder(R.drawable.kf_pic_thumb_bg)
                    .error(R.drawable.kf_image_download_fail_icon)
                    .into(holder.getIcon());

            holder.getMame().setText(cardInfo.name);

            holder.getTitle().setText(cardInfo.title);

            holder.getContent().setText(cardInfo.concent);

            holder.getRelativeLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(cardInfo.url);
                        intent.setData(content_url);
                        context.startActivity(intent);

                    }catch (Exception e){

                    }

                }
            });

            View.OnClickListener listener = ((ChatActivity) context).getChatAdapter().getOnClickListener();
            ViewHolderTag holderTag = ViewHolderTag.createTag(message, ViewHolderTag.TagType.TAG_SEND_MSG, position);
            holder.getSend().setTag(holderTag);
            holder.getSend().setOnClickListener(listener);

        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_card, null);
            CardViewHolder holder = new CardViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.CARDINFO_ROW_TRANSMIT.ordinal();
    }
}

