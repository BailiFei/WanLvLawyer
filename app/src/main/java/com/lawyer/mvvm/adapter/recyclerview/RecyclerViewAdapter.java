package com.lawyer.mvvm.adapter.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import com.lawyer.BR;

import java.util.List;

/**
 @author wang
 on 2019/2/22 */

public class RecyclerViewAdapter<T, Binding extends ViewDataBinding> extends BaseDataBindingAdapter<T, Binding> {


    public RecyclerViewAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public RecyclerViewAdapter(@Nullable List<T> data) {
        super(data);
    }

    public RecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(Binding bind, T item) {
        bind.setVariable(BR.bean, item);
    }
}
