package com.lawyer.mvvm.adapter.recyclerview;

import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 Created by wang
 on 2018/5/26 */
public class ViewAdapter {


    @BindingAdapter(value = {"datas", "item_layout", "loadMoreListener","itemDecoration"}, requireAll = false)
    public static void recyclerSetAdapter(RecyclerView recyclerView, List<?> data, int layoutId, BaseQuickAdapter.RequestLoadMoreListener loadMoreListener, RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
        RecyclerViewAdapter<?, ViewDataBinding> adapter = new RecyclerViewAdapter<>(layoutId, data);
        adapter.bindToRecyclerView(recyclerView);
        if (loadMoreListener != null) {
            adapter.setOnLoadMoreListener(loadMoreListener, recyclerView);
        }
    }
}
