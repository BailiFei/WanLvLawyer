package com.lawyer.mvvm.adapter.listview;

import android.databinding.BindingAdapter;
import android.widget.AbsListView;

import java.util.List;

/**
 Created by wang
 on 2018/5/26 */
public class ViewAdapter {

    @BindingAdapter(value = {"simpleAdapter"})
    public static void simpleAdapter(AbsListView listView, ListViewAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"datas", "item_layout", "variableId"}, requireAll = false)
    public static void simpleListView(AbsListView listView, List<?> data, int layoutId, int variableId) {
        listView.setAdapter(new ListViewAdapter<>(data, layoutId, variableId));
    }
}
