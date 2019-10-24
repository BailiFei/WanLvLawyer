package com.lawyer.mvvm.vm;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;

/**
 * Activity使用的ViewModel类
 * 持有的View为 activity
 * Created by wang on 2018/5/9.
 */
public class BaseActivityViewModel<MActivity extends VMBaseActivity, VD extends ViewDataBinding>
        extends BaseViewModel<MActivity, VD> {
    public BaseActivityViewModel(MActivity activity, VD binding) {
        super(activity, binding);
    }

    /** {@link Activity#onCreate(Bundle)}时调用该方法 ，可做一些初始化工作 */
    @Override
    public void onCreate() {

    }

    /** {@link Activity#onDestroy()}时调用该方法，可做些资源释放工作 */
    @Override
    public void onDestroy() {
        if (mViewWeakReference != null)
            mViewWeakReference.clear();

    }


    protected boolean checkNull() {
        return mViewWeakReference == null || getView() == null
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && (getView().isDestroyed() || getView().isFinishing()))
                || getView().isFinishing();
    }
}
