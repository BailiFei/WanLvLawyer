package com.lawyer.controller.payment;

import android.view.View;

import com.lawyer.databinding.FmPaymentBinding;
import com.lawyer.entity.OrderCreateBean;

/**
 @author wang
 on 2019/2/26 */

public class PaymentViewModel extends BasePayViewModel<PaymentFm, FmPaymentBinding> {
    public View.OnClickListener payClick = v -> commit();
    public View.OnClickListener welfareClick = v -> onSkip.put(1, true);

    public PaymentViewModel(PaymentFm paymentFm, FmPaymentBinding fmPaymentBinding) {
        super(paymentFm, fmPaymentBinding);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (key == 11) {
            orderCreateBean = (OrderCreateBean) object;

        }
    }




}
