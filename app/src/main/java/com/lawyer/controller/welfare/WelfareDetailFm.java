package com.lawyer.controller.welfare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.InputFilter;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsFm;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.account.LoginFm;
import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.databinding.FmWelfareDetailBinding;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.entity.WelfareBean;
import com.lawyer.net.Url;
import com.lawyer.util.UserCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.IToast;
import ink.itwo.common.util.etfilter.CashierInputFilter;

/**
 @author wang
 on 2019/2/13 */

public class WelfareDetailFm extends AbsFm<FmWelfareDetailBinding, WelfareDetailViewModel> {
    public static WelfareDetailFm newInstance(WelfareBean bean) {

        Bundle args = new Bundle();

        WelfareDetailFm fragment = new WelfareDetailFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }


    private List<BaseFragment> fragments = new ArrayList<>();
    private String[] titles = {"项目详情", "项目进展", "捐款记录"};
    private WelfareBean bean;
    @Override
    protected int initLayoutId() {
        return R.layout.fm_welfare_detail;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(true);
         bean = getArguments().getParcelable("bean");
        fragments.add(WelfareWebViewChildFm.newInstance(Url.h5ProjectDetail(bean.getId())));
        fragments.add(WelfareProgressChildFm.newInstance(bean));
        fragments.add(WelfareRecordChildFm.newInstance(bean));
        InputFilter[] inputFilter = {new CashierInputFilter()};
        bind.edit.setFilters(inputFilter);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
        bind.viewPager.setAdapter(adapter);
        bind.tabLayout.setupWithViewPager(bind.viewPager);
        putSkip(1, getArguments().getParcelable("bean"));
    }

    @Override
    protected void onSkip(int key, Object object) {
        if (key == 2) {
            //去捐助公益
            BigDecimal pay = (BigDecimal) object;
            BigDecimal targetMoney = bean.getTargetMoney();
            BigDecimal money = bean.getMoney();
            if (targetMoney != null && money != null && targetMoney.subtract(money).compareTo(pay) < 0) {
                IToast.show("已超过该捐助的目标金额");
                return;
            }

            if (UserCache.isLogged()) {
                toPay((BigDecimal) object);
                return;
            }
            LoginFm loginFm = LoginFm.newInstance();
            loginFm.setResultListener(isSuccess -> {
                if (isSuccess) toPay((BigDecimal) object);
            });
            mActivity.start(loginFm);
        }
    }

    private void toPay(BigDecimal object) {
        OrderCreateBean bean = new OrderCreateBean();
        bean.setMoney(object);
        bean.setType(PaymentFm.type_welfare);
        bean.setProjectId(this.bean.getId());
        bean.setOrderCreatePath(Url.orderCreateProjectOrder);
        mActivity.start(PaymentFm.newInstance(bean));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
//                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
//                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int initVariableId() {
        return BR.vm;
    }

    @Override
    protected WelfareDetailViewModel initViewModel() {
        return new WelfareDetailViewModel(this, bind);
    }
}
