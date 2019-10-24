package com.lawyer.mvvm.vm;


import android.databinding.BaseObservable;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.databinding.ViewDataBinding;

import java.lang.ref.WeakReference;

/**
 ViewModel的基类，继承自 {@link BaseObservable}
 Created by wang on 2018/5/9.
 @param <MView> 持有该ViewModel的View类，如Activity,Fragment,View等
 @param <VD>    系统生成的DataBinding类 */
public abstract class BaseViewModel<MView, VD extends ViewDataBinding>
        extends BaseObservable
        implements ISupportViewModel {
    protected WeakReference<MView> mViewWeakReference;
    protected VD bind;
    protected ObservableArrayMap<Integer,Object> onSkip = new ObservableArrayMap<Integer,Object>();

    public BaseViewModel(MView activity, VD vd) {
        mViewWeakReference = new WeakReference<>(activity);
        this.bind = vd;
        onSkip.addOnMapChangedCallback(new ObservableMap.OnMapChangedCallback<ObservableMap<Integer,Object>, Integer, Object>() {
            @Override
            public void onMapChanged(ObservableMap<Integer, Object> sender, Integer key) {
                putSkip(key, sender.get(key));
            }
        });
    }
    protected void putSkip(int key,Object object) {

    }

    /** 获取View、Activity,Fragment */
    protected MView getView() {
        return mViewWeakReference.get();
    }

    /** View 是否可用 true:View已为空，不可用的状态 */
    protected abstract boolean checkNull();


}
