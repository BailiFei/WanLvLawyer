package com.lawyer.controller.main;

import android.os.Bundle;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.account.LoginFm;
import com.lawyer.controller.main.vm.WelfareViewModel;
import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.controller.welfare.WelfareDetailFm;
import com.lawyer.databinding.FmMainWelfareBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.entity.WelfareBean;
import com.lawyer.net.Url;
import com.lawyer.util.UserCache;

import java.math.BigDecimal;

import ink.itwo.common.util.IToast;

/**
 公益
 @author wang
 on 2019/2/11 */

public class MainWelfareFm extends AbsFm<FmMainWelfareBinding, WelfareViewModel> {
    public static MainWelfareFm newInstance() {

        Bundle args = new Bundle();

        MainWelfareFm fragment = new MainWelfareFm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fm_main_welfare;
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected WelfareViewModel initViewModel() {
        return new WelfareViewModel(this, bind);
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 1) {
            //公益详情
            mActivity.start(WelfareDetailFm.newInstance((WelfareBean) object));
        } else if (key == 2) {
            //捐助公益
            WelfareBean welfareBean = (WelfareBean) object;
            BigDecimal targetMoney = welfareBean.getTargetMoney();
            BigDecimal money = welfareBean.getMoney();
            if (targetMoney != null && money != null && targetMoney.subtract(money).compareTo(BigDecimal.ONE) < 0) {
                IToast.show("已超过该捐助的目标金额");
                return;
            }

            if (UserCache.isLogged()) {
                toPay(welfareBean);
                return;
            }
            LoginFm loginFm = LoginFm.newInstance();
            loginFm.setResultListener(isSuccess -> {
                if (isSuccess) toPay((WelfareBean) object);
            });
            mActivity.start(loginFm);
        }
    }

    private void toPay(WelfareBean object) {
        if (UserCache.get().getIsVip() == 1) {
            //公益详情 会员到公益详情
            mActivity.start(WelfareDetailFm.newInstance((WelfareBean) object));
        } else {
            OrderCreateBean orderCreateBean = new OrderCreateBean();
            orderCreateBean.setProjectId(object.getId());
            orderCreateBean.setType(PaymentFm.type_member);
            orderCreateBean.setMoney(BigDecimal.ONE);
            orderCreateBean.setOrderCreatePath(Url.orderCreateProjectOrder);
            mActivity.start(PaymentFm.newInstance(orderCreateBean));
        }
    }
}
