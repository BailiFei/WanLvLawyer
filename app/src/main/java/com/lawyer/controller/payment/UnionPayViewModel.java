package com.lawyer.controller.payment;

import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.base.AbsViewModel;
import com.lawyer.databinding.FmPayUnionBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.util.LiveEventBus;
import com.lawyer.util.VerifyUtil;

import ink.itwo.common.util.IToast;

/**
 Created by wangtaian on 2019-06-06. */
public class UnionPayViewModel extends AbsViewModel<UnionPayFm, FmPayUnionBinding> {
    @Bindable
    public OrderCreateBean bean;

    public UnionPayViewModel(UnionPayFm fm, FmPayUnionBinding binding) {
        super(fm, binding);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (key == 11) {
            bean = (OrderCreateBean) object;
        }
    }

    public void commitClick(View view) {
        if (bean == null) {
            IToast.show("信息错误");
            return;
        }
        if (TextUtils.isEmpty(bean.getBkAcctNo())
                || TextUtils.isEmpty(bean.getIDNo())
                || TextUtils.isEmpty(bean.getCstmrNm())
                || !VerifyUtil.isMobile(bean.getMobNo())) {
            IToast.show("信息输入不完整");
            return;
        }
        UnionPayDialog payDialog = UnionPayDialog.newInstance( bean);
        payDialog.setCallback(new UnionPayDialog.UnionPayDialogCallback() {
            @Override
            public void onResult(boolean success,OrderCreateBean orderCreateBean) {
                if (success) {
                    LiveEventBus.get().with(UnionPayFm.BUS_UNION_PAY).postValue(orderCreateBean);
                    getView().pop();
                }
            }
        });
        payDialog.show(getView().getActivity().getSupportFragmentManager());
    }
}
