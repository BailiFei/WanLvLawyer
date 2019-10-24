package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.VoiceViewHolder;
import com.moor.imkf.db.dao.MessageDao;
import com.moor.imkf.http.FileDownLoadListener;
import com.moor.imkf.http.HttpManager;
import com.moor.imkf.model.entity.FromToMessage;

import java.io.File;
import java.util.UUID;

/**
 * Created by longwei on 2016/3/9.
 */
public class VoiceRxChatRow extends BaseChatRow {

    public VoiceRxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, final FromToMessage detail, final int position) {
        final VoiceViewHolder holder = (VoiceViewHolder) baseHolder;
        final FromToMessage message = detail;
        if (message != null) {
            if (message.unread2 != null && message.unread2.equals("1")) {
                ((VoiceViewHolder) baseHolder).voiceUnread.setVisibility(View.VISIBLE);
            } else {
                ((VoiceViewHolder) baseHolder).voiceUnread.setVisibility(View.GONE);
            }
            if (message.filePath == null || message.filePath.equals("")) {
                String dirStr = Environment.getExternalStorageDirectory() + File.separator + "cc/downloadfile/";
                File dir = new File(dirStr);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, "7moor_record_" + UUID.randomUUID() + ".amr");

                if (file.exists()) {
                    file.delete();
                }

                message.message = message.message.replaceAll("https://", "http://");

                HttpManager.downloadFile(message.message, file, new FileDownLoadListener() {
                    @Override
                    public void onSuccess(File file) {
                        String filePath = file.getAbsolutePath();
                        message.filePath = filePath;
                        MessageDao.getInstance().updateMsgToDao(message);
                        VoiceViewHolder.initVoiceRow(holder, detail, position, (ChatActivity) context, true);
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

            } else {
                holder.initVoiceRow(holder, detail, position, (ChatActivity) context, true);
            }
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_voice_rx, null);
            VoiceViewHolder holder = new VoiceViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.VOICE_ROW_RECEIVED.ordinal();
    }
}
