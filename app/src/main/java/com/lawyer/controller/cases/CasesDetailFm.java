package com.lawyer.controller.cases;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.controller.cases.vm.CasesDetailViewModel;
import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.controller.payment.PaymentResultFm;
import com.lawyer.databinding.FmCaseDetailBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.entity.User;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.net.Url;
import com.lawyer.util.LiveEventBus;
import com.lawyer.util.UserCache;

import java.math.BigDecimal;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 案例详情
 @author wang
 on 2019/2/12 */

public class CasesDetailFm extends AbsFm<FmCaseDetailBinding, CasesDetailViewModel> {
    public static CasesDetailFm newInstance(UserCaseBean bean, boolean isTitleEnable) {

        Bundle args = new Bundle();

        CasesDetailFm fragment = new CasesDetailFm();
        args.putParcelable("bean", bean);
        args.putBoolean("isTitleEnable", isTitleEnable);
        fragment.setArguments(args);
        return fragment;
    }

    private UserCaseBean caseBean;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_case_detail;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("案件详情");
        View layoutTitle = viewRoot.findViewById(R.id.layout_title);
        boolean isTitleEnable = getArguments().getBoolean("isTitleEnable");
        layoutTitle.setVisibility(isTitleEnable ? View.VISIBLE : View.GONE);
        caseBean = getArguments().getParcelable("bean");
        putSkip(11, caseBean);
        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT, Boolean.class)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            caseBean.setNeedPayMoney(BigDecimal.ZERO);
                            putSkip(11, caseBean);
                        }
                    }
                });
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 2) {
            //用户支付案件费用
            OrderCreateBean bean = new OrderCreateBean();
            if (caseBean != null) {
                bean.setMoney(caseBean.getNeedPayMoney());
            }
            bean.setType(PaymentFm.type_case);
            bean.setCaseId(caseBean.getCaseId());
            bean.setOrderCreatePath(Url.orderCreateCaseOrder);
            mActivity.start(PaymentFm.newInstance(bean));
            return;
        }
        if (key == 3 && caseBean != null && caseBean.getState() == CaseStateEnum.tendering) {
            //律师投标
            NetManager.http().create(API.class)
                    .caseApplyCase(UserCache.getAccessToken(), caseBean.getId())
                    .compose(NetTransformer.handle(this))
                    .subscribe(new ResultObserver<Result>() {
                        @Override
                        public void onNext(Result result) {
                            IToast.show("投标成功");
                            caseBean.setState(CaseStateEnum.solving);
                            User lawyer = UserCache.get();
                            caseBean.setLawyerId(lawyer.getId());
                            caseBean.setLawyerName(lawyer.getNickName());
                            caseBean.setLawyerMobileNo(lawyer.getMobileNo());
                            caseBean.setLawyerAvatarUrl(lawyer.getAvatarUrl());
                            putSkip(11, caseBean);
                        }
                    });
        }
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected CasesDetailViewModel initViewModel() {
        return new CasesDetailViewModel(this, bind);
    }
}
