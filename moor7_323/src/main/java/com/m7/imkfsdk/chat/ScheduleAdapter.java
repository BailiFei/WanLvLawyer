package com.m7.imkfsdk.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.ScheduleConfig;

import java.util.List;

/**
 * Created by pangw on 2018/3/6.
 */

public class ScheduleAdapter extends BaseAdapter {

    private List<ScheduleConfig.EntranceNodeBean.EntrancesBean> peers;

    private Context context;

    public ScheduleAdapter(Context context, List<ScheduleConfig.EntranceNodeBean.EntrancesBean> peers) {
        this.context = context;
        this.peers = peers;
    }

    @Override
    public int getCount() {
        return peers.size();
    }

    @Override
    public Object getItem(int position) {
        return peers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleAdapter.ViewHolder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.kf_investigate_list, null);
            holder = new ScheduleAdapter.ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.investigate_list_tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ScheduleAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(peers.get(position).getName());
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_name;
    }
}
