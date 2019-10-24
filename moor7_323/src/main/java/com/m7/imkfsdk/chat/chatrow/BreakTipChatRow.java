package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.BreakTipHolder;
import com.m7.imkfsdk.chat.holder.InvestigateViewHolder;
import com.m7.imkfsdk.chat.holder.TextViewHolder;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.utils.NullUtil;

/**
 * Created by longwei on 2017/12/11.
 */

public class BreakTipChatRow extends BaseChatRow{


    public BreakTipChatRow(int type) {
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_break_tip_rx, null);
            BreakTipHolder holder = new BreakTipHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.BREAK_TIP_ROW_RECEIVED.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(Context context, BaseHolder baseHolder, FromToMessage detail, int position) {

        BreakTipHolder holder = (BreakTipHolder) baseHolder;
        final FromToMessage message = detail;
        if (message != null) {
            holder.getDescTextView().setText(NullUtil.checkNull(message.message));
        }
    }
}
