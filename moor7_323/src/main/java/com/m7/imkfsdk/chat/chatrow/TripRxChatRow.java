package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.BreakTipHolder;
import com.m7.imkfsdk.chat.holder.TripHolder;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.utils.NullUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by longwei on 2017/12/11.
 */

public class TripRxChatRow extends BaseChatRow{


    public TripRxChatRow(int type) {
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_trip_rx, null);
            TripHolder holder = new TripHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.TRIP_ROW_RECEIVED.ordinal();
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(Context context, BaseHolder baseHolder, FromToMessage detail, int position) {

        TripHolder holder = (TripHolder) baseHolder;
        final FromToMessage message = detail;
        if (message != null) {
//            holder.getDescTextView().setText(NullUtil.checkNull(message.message));

            String classifyName = "";
            try {
                JSONObject jsonObject = new JSONObject(message.message);
                JSONArray detailList = jsonObject.getJSONArray("details");
                for (int i=0; i<detailList.length(); i++) {
                    JSONObject jb = detailList.getJSONObject(i);
                    classifyName += jb.getString("classifyName");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.getDescTextView().setText(NullUtil.checkNull(classifyName));
        }
    }
}
