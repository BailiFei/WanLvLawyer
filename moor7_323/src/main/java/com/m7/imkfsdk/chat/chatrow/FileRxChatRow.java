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
import com.moor.imkf.FileMessageDownLoadListener;
import com.moor.imkf.IMChat;
import com.moor.imkf.model.entity.FromToMessage;

import java.io.File;

/**
 * Created by longwei on 2016/3/10.
 */
public class FileRxChatRow extends BaseChatRow {

    public FileRxChatRow(int type) {
        super(type);
    }

    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu, View targetView, FromToMessage detail) {
        return false;
    }

    @Override
    protected void buildChattingData(final Context context, BaseHolder baseHolder, FromToMessage detail, int position) {
        final FileViewHolder holder = (FileViewHolder) baseHolder;
        final FromToMessage message = detail;
        if(message != null) {

            if(message.withDrawStatus) {
                holder.getWithdrawTextView().setVisibility(View.VISIBLE);
                holder.getContainer().setVisibility(View.GONE);
            }else {
                holder.getWithdrawTextView().setVisibility(View.GONE);
                holder.getContainer().setVisibility(View.VISIBLE);

                holder.getChat_content_tv_name().setText(message.fileName);
                holder.getChat_content_tv_size().setText(message.fileSize);
                holder.getChat_content_tv_status().setText(message.fileDownLoadStatus);
                holder.getChat_content_pb_progress().setProgress(message.fileProgress);

                if("success".equals(message.fileDownLoadStatus)) {
                    holder.getChat_content_pb_progress().setVisibility(View.GONE);
                    holder.getChat_content_tv_status().setVisibility(View.VISIBLE);
                    holder.getChat_content_tv_status().setText(R.string.haddownload);
                    holder.getChat_content_iv_download().setVisibility(View.GONE);

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
                                    intent.setDataAndType(contentUri,  MimeTypesTools.getMimeType(context, message.fileName));
                                } else {
                                    intent.setDataAndType(Uri.fromFile(file),  MimeTypesTools.getMimeType(context, message.fileName));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                }
                                context.startActivity(intent);
                            } catch (Exception e) {
                            }

                        }
                    });
                }else if("failed".equals(message.fileDownLoadStatus)) {
                    holder.getChat_content_pb_progress().setVisibility(View.GONE);
                    holder.getChat_content_tv_status().setVisibility(View.GONE);
                    holder.getChat_content_iv_download().setVisibility(View.VISIBLE);
                }else if("downloading".equals(message.fileDownLoadStatus)) {
                    holder.getChat_content_pb_progress().setVisibility(View.VISIBLE);
                    holder.getChat_content_tv_status().setVisibility(View.VISIBLE);
                    holder.getChat_content_tv_status().setText(R.string.downloading);
                    holder.getChat_content_iv_download().setVisibility(View.GONE);
                }

                holder.getChat_content_iv_download().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.getChat_content_pb_progress().setVisibility(View.VISIBLE);
                        holder.getChat_content_tv_status().setVisibility(View.VISIBLE);
                        holder.getChat_content_tv_status().setText(R.string.downloading);
                        holder.getChat_content_iv_download().setVisibility(View.GONE);

                        IMChat.getInstance().downLoadFile(message, new FileMessageDownLoadListener() {
                            @Override
                            public void onSuccess(File file) {
                                ((ChatActivity)context).getChatAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed() {
                                ((ChatActivity)context).getChatAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onProgress() {
                                ((ChatActivity)context).getChatAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.kf_chat_row_file_rx, null);
            FileViewHolder holder = new FileViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
        return convertView;
    }

    @Override
    public int getChatViewType() {
        return ChatRowType.FILE_ROW_RECEIVED.ordinal();
    }
}
