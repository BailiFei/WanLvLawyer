package com.lawyer.base;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lawyer.R;
import com.umeng.analytics.MobclickAgent;

import ink.itwo.common.ctrl.CommonFragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 @author wang
 on 2019/1/30 */

public abstract class BaseFragment<MActivity extends BaseActivity> extends CommonFragment<MActivity> {
    protected MActivity mActivity;
    protected CompositeDisposable disposableLife;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MActivity) _mActivity;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        View back = viewRoot.findViewById(R.id.iv_back_layout);
        if (back != null) {
            back.setOnClickListener(v -> pop());
        }
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposableLife != null) {
            disposableLife.clear();
        }
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    public void addDisposableLife(Disposable... disposable) {
        if (disposableLife==null) disposableLife = new CompositeDisposable();
        disposableLife.addAll(disposable);
    }

    @Nullable
    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == -1 || viewRoot == null) {
            return null;
        }
        return viewRoot.findViewById(id);
    }

    public BaseFragment setTitle(CharSequence string) {
        return setTitle(string, 0);
    }

    public BaseFragment setTitle(CharSequence string, @ColorInt int color) {
        if (viewRoot == null) return this;
        TextView title = viewRoot.findViewById(R.id.tv_title_layout);
        if (title == null) return this;
        title.setText(string);
        if (color != 0) title.setTextColor(color);
        return this;
    }

    public BaseFragment setTitleBackgroundColor(@ColorInt int color) {
        if (viewRoot == null) return this;
        RelativeLayout relativeLayout = viewRoot.findViewById(R.id.layout_title);
        if (relativeLayout == null) return this;
        relativeLayout.setBackgroundColor(color);
        return this;
    }

    public BaseFragment setTitleBarText(CharSequence string, View.OnClickListener clickListener) {
        if (viewRoot == null) return this;
        TextView tvClick = viewRoot.findViewById(R.id.tv_click_layout);
        if (tvClick == null) return this;
        tvClick.setText(string);
        if (clickListener != null) {
            tvClick.setOnClickListener(clickListener);
        }
        return this;
    }

    public BaseFragment setTitleBarImage(int resId, View.OnClickListener clickListener) {
        if (viewRoot == null) return this;
        ImageView imageView = viewRoot.findViewById(R.id.iv_right_layout);
        if (imageView == null) return this;
        imageView.setImageResource(resId);
        return this;
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //请在onSupportVisible实现沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).keyboardEnable(true).init();
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

}
