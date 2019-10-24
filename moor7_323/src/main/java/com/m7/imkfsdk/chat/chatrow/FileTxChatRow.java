package com.m7.imkfsdk.chat.chatrow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.m7.imkfsdk.BuildConfig;
import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.holder.BaseHolder;
import com.m7.imkfsdk.chat.holder.FileViewHolder;
import com.m7.imkfsdk.utils.MimeTypesTools;
import com.moor.imkf.model.entity.FromToMessage;

import java.io.File;

/**
 * Created by longwei on 2016/3/10.
 */
public class FileTxChatRow extends BaseChatRow {


    public FileTxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        FileViewHolder holder = (FileViewHolder) baseHolder;
        final FromToMessage message = detail;
        if (message != null) {

            holder.getChat_content_tv_name().setText(message.fileName);
            holder.getChat_content_tv_size().setText(message.fileSize);
            holder.getChat_content_tv_status().setText(message.fileUpLoadStatus);
            holder.getChat_content_pb_progress().setProgress(message.fileProgress);

            View.OnClickListener listener = ((ChatActivity) context).getChatAdapter().getOnClickListener();
            getMsgStateResId(position, holder, message, listener);

            if ("true".equals(message.sendState)) {
                holder.getChat_content_tv_status().setText(R.string.sended);
                holder.getChat_content_pb_progress().setVisibility(View.GONE);

                holder.getBaseView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            File file = new File(message.filePath);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setAction(Intent.ACTION_VIEW);
                                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                                intent.setDataAndType(contentUri, MimeTypesTools.getMimeType(context, message.fileName));
                            } else {
                                intent.setDataAndType(Uri.fromFile(file), MimeTypesTools.getMimeType(context, message.fileName));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_file_tx, null);
            FileViewHolder holder = new FileViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));
        }

        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.FILE_ROW_TRANSMIT.ordinal();
    }
}
