package com.lawyer.controller.payment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;

/**
 支付结果
 @author wang
 on 2019/2/14 */

public class WithdrawResultFm extends BaseFragment<MainActivity> {
    public static WithdrawResultFm newInstance(String orderId, String amount) {

        Bundle args = new Bundle();

        WithdrawResultFm fragment = new WithdrawResultFm();
        args.putString("orderId", orderId);
        args.putString("amount", amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_withdraw_result;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("提现结果");
        setSwipeBackEnable(true);
        String orderId = getArguments().getString("orderId");
        if (!TextUtils.isEmpty(orderId)) {
            TextView tvOrderId = findViewById(R.id.tv_order_no);
            tvOrderId.setText(orderId);
        }
        String amount = getArguments().getString("amount");
        if (amount != null) {
            TextView tvAmount = findViewById(R.id.tv_amout);
            tvAmount.setText(amount+"元");
        }

        findViewById(R.id.tv_confirm).setOnClickListener(v -> pop());
    }
}
