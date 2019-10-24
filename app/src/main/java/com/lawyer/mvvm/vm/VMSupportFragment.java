package com.lawyer.mvvm.vm;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawyer.base.BaseActivity;
import com.lawyer.base.BaseFragment;


/**
 支持ViewModel的Fragment
 Created by wang on 2018/5/9. */
public abstract class VMSupportFragment
        <VD extends ViewDataBinding, VM extends BaseFmViewModel, MActivity extends BaseActivity>
        extends BaseFragment<MActivity> {
    protected VD bind;
    protected VM vm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, initLayoutId(), container, false);
        bind.setVariable(initVariableId(), vm = initViewModel());
        viewRoot = bind.getRoot();
        onCreateView(viewRoot);
        vm.onSkip.addOnMapChangedCallback(changedCallback);
        return viewRoot;
//        return isSwipeBackEnable ? attachToSwipeBack(viewRoot) : viewRoot;
    }


    private ObservableArrayMap.OnMapChangedCallback changedCallback = new ObservableMap.OnMapChangedCallback<ObservableArrayMap<Integer, Object>, Integer, Object>() {
        @Override
        public void onMapChanged(ObservableArrayMap<Integer, Object> sender, Integer key) {
            onSkip(key, sender.get(key));
        }

    };
   protected void onSkip(int key,Object object) {

    }
    public void putSkip(int key,Object object) {
        vm.onSkip.put(key, object);
    }

//    protected void onSkip(ObservableInt sender, int propertyId) {
//
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm.onCreate();
    }

    @Override
    public void onDestroyView() {
        if (vm != null) {
            vm.onSkip.removeOnMapChangedCallback(changedCallback);
            vm.onDestroy();
            vm = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (vm != null) {
            vm.onSkip.removeOnMapChangedCallback(changedCallback);
            vm.onDestroy();
            vm = null;
        }
        super.onDestroy();
    }

    /** ViewModel的Id，对应布局文件中设置的 variable */
    protected abstract int initVariableId();

    /** 初始化 ViewModel */
    protected abstract VM initViewModel();
}
