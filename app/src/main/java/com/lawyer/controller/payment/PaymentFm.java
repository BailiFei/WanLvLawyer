package com.lawyer.controller.payment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.main.MainFm;
import com.lawyer.databinding.FmPaymentBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.util.LiveEventBus;

/**
 订单支付
 @author wang
 on 2019/2/14 */

public class PaymentFm extends AbsFm<FmPaymentBinding, PaymentViewModel> {
    //    1:一元公益，2：充值,3:案件详情支付,4：开通会员
    public static final int type_welfare = 1, type_top_up = 2, type_case = 3, type_member = 4;
    public static SparseArray<String> type_pay_result_category = new SparseArray<String>() {
        {
            put(type_welfare, "捐款");
            put(type_top_up, "充值");
            put(type_case, "案件费用支付");
            put(type_member, "1元开通会员服务");
        }
    };

    public static final String WECHAT_PAY_RESULT = "WECHAT_PAY_RESULT";

    public static PaymentFm newInstance(OrderCreateBean bean) {

        Bundle args = new Bundle();

        PaymentFm fragment = new PaymentFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_payment;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("订单支付");
        setSwipeBackEnable(true);
        OrderCreateBean orderCreateBean = getArguments().getParcelable("bean");
        putSkip(11, orderCreateBean);
        findViewById(R.id.tv_to_welfare).setOnClickListener(v -> {
            mActivity.popTo(MainFm.class, false);
            MainFm fragment = mActivity.findFragment(MainFm.class);
            if (fragment != null) {
                fragment.onPerformClick(MainFm.welfare);
            }
        });
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
    public void pop() {
        super.pop();
        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
    }

    @Override
    protected void onSkip(int key, Object object) {
        //了解公益，直接跳转到主页的公益模块
        if (key == 1) {
            MainFm fragment = mActivity.findFragment(MainFm.class);
            if (fragment != null) {
                fragment.onPerformClick(MainFm.welfare);
            }
        }
        if (key == 2) {
            //支付成功，跳转到支付成功提示界面
            mActivity.startWithPop(PaymentResultFm.newInstance((OrderCreateBean) object));
        }
        if (key == 3) {
            //畅捷支付
            mActivity.start(UnionPayFm.newInstance((OrderCreateBean) object));
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected PaymentViewModel initViewModel() {
        return new PaymentViewModel(this, bind);
    }
}
