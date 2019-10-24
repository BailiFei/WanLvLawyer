package com.lawyer.base;

import android.databinding.ViewDataBinding;

import com.lawyer.mvvm.vm.BaseFmViewModel;
import com.lawyer.mvvm.vm.VMSupportFragment;

/**
 @author wang
 on 2019/2/25 */

public class AbsViewModel<FM extends VMSupportFragment, VD extends ViewDataBinding> extends BaseFmViewModel<FM, VD> {
    public AbsViewModel(FM fm, VD vd) {
        super(fm, vd);
    }
    public void getInfo() {

    }
}
