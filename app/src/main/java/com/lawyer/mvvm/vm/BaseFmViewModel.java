package com.lawyer.mvvm.vm;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 继承自{@link BaseViewModel}
 * 持有的View是Fragment
 * Created by wang on 2018/5/9.
 */
public class BaseFmViewModel<FM extends VMSupportFragment, VD extends ViewDataBinding>
        extends BaseViewModel<FM, VD> {

    public BaseFmViewModel(FM fm, VD vd) {
        super(fm, vd);
    }

    /** {@link Fragment#onCreate(Bundle)}时调用该方法 ，可做一些初始化工作 */
    @Override
    public void onCreate() {

    }

    /** {@link Fragment#onDestroyView()} ()}时调用该方法，可做些资源释放工作 */
    @Override
    public void onDestroy() {
        if (mViewWeakReference != null)
            mViewWeakReference.clear();
    }


    @Override
    protected boolean checkNull() {
        return mViewWeakReference == null
                || getView() == null
                || getView().getActivity() == null
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && (getView().getActivity().isFinishing() || getView().getActivity().isDestroyed()))
                || getView().getActivity().isFinishing();
    }

    protected VMBaseActivity scanForActivity(Context cont){
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (VMBaseActivity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }
}
