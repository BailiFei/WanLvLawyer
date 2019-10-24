package com.lawyer.controller.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.main.MainFm;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.util.LiveEventBus;

/**
 支付结果
 @author wang
 on 2019/2/14 */

public class PaymentResultFm extends BaseFragment<MainActivity> {
    public static final String BUS_PAYMENT = "BUS_PAYMENT";

    public static PaymentResultFm newInstance(OrderCreateBean bean) {

        Bundle args = new Bundle();

        PaymentResultFm fragment = new PaymentResultFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_payment_result;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("支付结果");
        setSwipeBackEnable(true);
        OrderCreateBean bean = getArguments().getParcelable("bean");
        View tvToWelfare = findViewById(R.id.tv_to_welfare);
        tvToWelfare.setOnClickListener(v -> {
            mActivity.popTo(MainFm.class, false);
            MainFm fragment = mActivity.findFragment(MainFm.class);
            if (fragment != null) {
                fragment.onPerformClick(MainFm.welfare);
            }
        });
        if (bean != null) {
            TextView tvOrderNo = viewRoot.findViewById(R.id.tv_order_no);
            TextView tvMoney = viewRoot.findViewById(R.id.tv_money);
            tvOrderNo.setText(bean.getOrderNo());
            tvMoney.setText(bean.getMoney() == null ? "" : "¥" + bean.getMoney().toString());

            tvToWelfare.setVisibility(bean.getType() == PaymentFm.type_welfare ? View.VISIBLE : View.GONE);

            findViewById(R.id.tv_commit).setOnClickListener(v -> pop());
            TextView tvCategory = findViewById(R.id.tv_category);
            if (bean.getType() != 0) {
                tvCategory.setText(PaymentFm.type_pay_result_category.get(bean.getType()));

            }
        }
    }

    @Override
    public void pop() {
        super.pop();
        LiveEventBus.get().with(BUS_PAYMENT).setValue(true);
    }
}
