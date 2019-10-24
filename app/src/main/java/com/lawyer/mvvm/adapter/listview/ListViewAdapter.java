package com.lawyer.mvvm.adapter.listview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lawyer.BR;

import java.util.List;

/**
 * Created by wang
 * on 2018/5/26
 */
public class ListViewAdapter<T, ItemBinding extends ViewDataBinding> extends BaseAdapter {
    private List<T> data;
    private int layoutId;
    private int variableId;
    private ItemBinding binding;

    public ListViewAdapter(List<T> data, int layoutId, int variableId) {
        this.data = data;
        this.layoutId = layoutId;
        this.variableId = variableId;
        if (this.variableId <= 0) {
            this.variableId = BR.bean;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemBinding) convertView.getTag();
        }
        binding.setVariable(variableId, data.get(position));
        binding.executePendingBindings();
        convert(binding, data.get(position));
        return convertView;
    }

    public void convert(ItemBinding bind, T item) {

    }
}
