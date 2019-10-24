package com.lawyer.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lawyer.MainActivity;
import com.lawyer.mvvm.vm.VMSupportFragment;

/**
 @author wang
 on 2019/2/25 */

public abstract class AbsFm<VD extends ViewDataBinding, VM extends AbsViewModel> extends VMSupportFragment<VD, VM, MainActivity> {

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (vm!=null) vm.getInfo();
    }
}
