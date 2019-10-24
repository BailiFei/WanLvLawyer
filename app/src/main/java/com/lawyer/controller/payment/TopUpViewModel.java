package com.lawyer.controller.payment;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.databinding.FmTopUpBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.net.Url;

import java.math.BigDecimal;

import ink.itwo.common.util.IToast;

/**
 Created by wangtaian on 2019/4/8. */
public class TopUpViewModel extends BasePayViewModel<TopUpFm, FmTopUpBinding> {
    @Bindable
    public ObservableField<String> money = new ObservableField<>();
    public View.OnClickListener payClick = v -> {
        if (TextUtils.isEmpty(money.get())) {
            IToast.show("请输入充值金额");
            return;
        }
        BigDecimal bigDecimal = new BigDecimal(money.get());
        orderCreateBean.setMoney(bigDecimal);
        commit();
    };


    public TopUpViewModel(TopUpFm fm, FmTopUpBinding binding) {
        super(fm, binding);
        orderCreateBean = new OrderCreateBean();
        orderCreateBean.setType(PaymentFm.type_top_up);
        orderCreateBean.setOrderCreatePath(Url.orderCreateCapitalOrder);
    }
}
