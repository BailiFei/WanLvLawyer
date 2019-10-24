package com.lawyer.controller.payment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.databinding.FmTopUpBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.util.LiveEventBus;
import com.lianlian.base.OnResultListener;
import com.lianlian.securepay.token.SecurePayService;

/**
 充值界面
 @author wang
 on 2019/2/20 */

public class TopUpFm extends AbsFm<FmTopUpBinding, TopUpViewModel> {
    public static TopUpFm newInstance() {

        Bundle args = new Bundle();

        TopUpFm fragment = new TopUpFm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_top_up;
    }
    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("订单支付");
        setSwipeBackEnable(true);
        LiveEventBus.get().with(UnionPayFm.BUS_UNION_PAY, OrderCreateBean.class)
                .observe(this, new Observer<OrderCreateBean>() {
                    @Override
                    public void onChanged(@Nullable OrderCreateBean bean) {
                        if (bean != null ) {
                            //支付成功，跳转到支付成功提示界面
                            mActivity.startWithPop(PaymentResultFm.newInstance(bean));
                        }
                    }
                });
    }
    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected TopUpViewModel initViewModel() {
        return new TopUpViewModel(this, bind);
    }

    @Override
    public void pop() {
        super.pop();
        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
    }

    @Override
    protected void onSkip(int key, Object object) {
//        //了解公益，直接跳转到主页的公益模块
//        if (key == 1) {
//            MainFm fragment = mActivity.findFragment(MainFm.class);
//            if (fragment != null) {
//                fragment.onPerformClick(MainFm.welfare);
//            }
//        }
        if (key == 2) {
            //支付成功，跳转到支付成功提示界面
            mActivity.startWithPop(PaymentResultFm.newInstance((OrderCreateBean) object));
        }
        if (key == 3) {
            //畅捷支付
            mActivity.start(UnionPayFm.newInstance((OrderCreateBean) object));
        }
    }
}
