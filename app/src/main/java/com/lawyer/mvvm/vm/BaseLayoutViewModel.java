package com.lawyer.mvvm.vm;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.view.View;

/**
 * 继承自{@link BaseViewModel}
 * 持有的View是View，可用在自定义View,ListView的Item中
 * Created by wang
 * on 2018/6/1
 */
public class BaseLayoutViewModel<MView extends View, VD extends ViewDataBinding>
        extends BaseViewModel<MView, VD> {

    public BaseLayoutViewModel(MView activity, VD vd) {
        super(activity, vd);
    }

    /** {@link View#onAttachedToWindow()}或者View创建时调用该方法 */
    @Override
    public void onCreate() {

    }

    /** {@link View#onDetachedFromWindow()}时调用该方法，可做释放资源工作 */
    @Override
    public void onDestroy() {
        if (mViewWeakReference != null)
            mViewWeakReference.clear();
    }

    @Override
    protected boolean checkNull() {
        if (mViewWeakReference == null || getView() == null) {
            return true;
        }
        VMBaseActivity activity = scanForActivity(getView().getContext());
        if (activity == null) {
            return true;
        }
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && (activity.isDestroyed() || activity.isFinishing()))
                || activity.isFinishing();
    }

    /** Context 转换成 Activity */
    protected VMBaseActivity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (VMBaseActivity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }

}
