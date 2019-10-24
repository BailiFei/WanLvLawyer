package com.lawyer.controller.payment;

import android.os.Bundle;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.databinding.FmPayUnionBinding;
import com.lawyer.entity.OrderCreateBean;

/**
 畅捷支付-银联支付
 Created by wangtaian on 2019-06-06. */
public class UnionPayFm extends AbsFm<FmPayUnionBinding, UnionPayViewModel> {
    public static UnionPayFm newInstance(OrderCreateBean bean) {

        Bundle args = new Bundle();

        UnionPayFm fragment = new UnionPayFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String BUS_UNION_PAY = "BUS_UNION_PAY";

    @Override
    protected int initLayoutId() {
        return R.layout.fm_pay_union;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("银联支付");
        putSkip(11, getArguments().getParcelable("bean"));

    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected UnionPayViewModel initViewModel() {
        return new UnionPayViewModel(this, bind);
    }
}
