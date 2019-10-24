package com.m7.imkfsdk.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.ChatMore;

import java.util.List;

/**
 * 更多文件填充器
 */
public class MoreAdapter extends BaseAdapter {

	private List<ChatMore> chatMoreList;
	private LayoutInflater inflater;
	private int size = 0;
	private Context context;
	private Handler handler;

	public MoreAdapter(Context context, List<ChatMore> chatMoreList,
					   Handler handler) {
		this.inflater = LayoutInflater.from(context);
		this.chatMoreList = chatMoreList;
		this.size = chatMoreList.size();
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return this.size;
	}

	@Override
	public Object getItem(int position) {
		return chatMoreList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChatMore chatMore = chatMoreList.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.kf_viewpager_item_more, null);
			viewHolder.mItemIvMoreImg = (ImageView) convertView
					.findViewById(R.id.item_iv_more_img);
			viewHolder.mItemIvMoreText = (TextView) convertView
					.findViewById(R.id.item_iv_more_text);
			viewHolder.mItemIvMoreLinear = (LinearLayout) convertView
					.findViewById(R.id.item_iv_more_linear);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.mItemIvMoreImg.setTag(chatMore);
		if (TextUtils.isEmpty(chatMore.imgurl)) {
			convertView.setBackgroundDrawable(null);
			viewHolder.mItemIvMoreImg.setVisibility(View.GONE);
		} else {
			viewHolder.mItemIvMoreText.setText(chatMore.name);
			viewHolder.mItemIvMoreImg.setImageResource(Integer
					.parseInt(chatMore.imgurl));
		}
		viewHolder.mItemIvMoreLinear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChatMore chatMore = chatMoreList.get(position);
				Message msg = new Message();
				msg.obj = chatMore.name;
				handler.sendMessage(msg);
			}
		});

		return convertView;
	}

	public class ViewHolder {

		public ImageView mItemIvMoreImg;
		public TextView mItemIvMoreText;
		public LinearLayout mItemIvMoreLinear;
	}
}