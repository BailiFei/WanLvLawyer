package com.m7.imkfsdk.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.Peer;

import java.util.List;

/**
 * 评价列表adapter
 */
public class PeerAdapter extends BaseAdapter {

    private List<Peer> peers;

    private Context context;

    public PeerAdapter(Context context, List<Peer> peers) {
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
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.kf_investigate_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.investigate_list_tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(peers.get(position).getName());
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_name;
    }
}
