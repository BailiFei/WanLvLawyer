package com.lawyer.dialog;

import android.os.Bundle;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.net.Url;

import java.math.BigDecimal;

import ink.itwo.common.ctrl.CommonDialog;
import ink.itwo.common.util.DeviceUtil;

/**
 @author wang
 on 2019/3/1 */

public class MemberHintDialog extends CommonDialog< MainActivity> {
    public static MemberHintDialog newInstance() {

        Bundle args = new Bundle();

        MemberHintDialog fragment = new MemberHintDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dg_member_hint;
    }

    @Override
    public void convertView(View view) {
        setWidth(DeviceUtil.getDimensionPx(R.dimen.dp_298)).setHeight(DeviceUtil.getDimensionPx(R.dimen.dp_450));
        view.findViewById(R.id.iv_close)
                .setOnClickListener(v -> dismiss());
        view.findViewById(R.id.tv_commit)
                .setOnClickListener(v -> {
//                    mActivity.start(RealNameFm.newInstance());
                    OrderCreateBean orderCreateBean = new OrderCreateBean();
                    orderCreateBean.setOrderCreatePath(Url.orderCreateProjectOrder);
                    orderCreateBean.setMoney(BigDecimal.ONE);
                    orderCreateBean.setProjectId(1);
                    orderCreateBean.setType(PaymentFm.type_member);
                    mActivity.start(PaymentFm.newInstance(orderCreateBean));
                    dismiss();
                });
    }
}
