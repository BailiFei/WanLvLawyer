package com.lawyer.controller.payment;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.databinding.FmWithdrawBinding;
import com.lawyer.entity.CapitalWithdrawBean;
import com.lawyer.util.MaxInputFilter;

import java.math.BigDecimal;

/**
 提现
 @author wang
 on 2019/2/14 */

public class WithdrawFm extends AbsFm<FmWithdrawBinding, WithdrawViewModel> {
    public static WithdrawFm newInstance(BigDecimal moneyMax) {

        Bundle args = new Bundle();

        WithdrawFm fragment = new WithdrawFm();
        args.putSerializable("moneyMax", moneyMax);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_withdraw;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("提现");
        setSwipeBackEnable(true);
        BigDecimal moneyMax = (BigDecimal) getArguments().getSerializable("moneyMax");
        putSkip(1, moneyMax);
//        InputFilter[] inputFilter = {new MaxInputFilter(moneyMax)};
//        bind.edit.setFilters(inputFilter);
//        findViewById(R.id.tv_commit).setOnClickListener(v ->
//                mActivity.start(WithdrawResultFm.newInstance()));
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 11) {
            CapitalWithdrawBean bean = (CapitalWithdrawBean) object;
            if (bean==null) bean = new CapitalWithdrawBean();
            mActivity.startWithPop(WithdrawResultFm.newInstance(bean.getWithdrawOrderId(),bean.getApplyAmount()));
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected WithdrawViewModel initViewModel() {
        return new WithdrawViewModel(this, bind);
    }
}
