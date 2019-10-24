package com.lawyer.mvvm.vm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lawyer.base.BaseActivity;


/**支持ViewModel的Activity
 * Created by wang on 2018/5/9.
 */
public abstract class VMBaseActivity<VD extends ViewDataBinding, VM extends BaseActivityViewModel> extends BaseActivity {
    protected VD bind;
    protected VM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewDataBinding();
        vm.onCreate();
    }

    private void initViewDataBinding() {
        bind = DataBindingUtil.setContentView(this, initContentView());
        bind.setVariable(initVariableId(), vm = initViewModel());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vm.onDestroy();
        vm = null;
    }

    /** Activity的布局文件，对应 {@link android.app.Activity#setContentView(int)} */
    public abstract int initContentView();

    /** ViewModel的Id，对应布局文件中设置的 variable */
    public abstract int initVariableId();

    /** 初始化ViewModel */
    public abstract VM initViewModel();


}