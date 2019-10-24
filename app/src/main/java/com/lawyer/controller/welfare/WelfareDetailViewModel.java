package com.lawyer.controller.welfare;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.base.AbsViewModel;
import com.lawyer.databinding.FmWelfareDetailBinding;
import com.lawyer.entity.WelfareBean;

import java.math.BigDecimal;
import java.text.NumberFormat;

import ink.itwo.common.util.IToast;

/**
 @author wang
 on 2019/2/26 */

public class WelfareDetailViewModel extends AbsViewModel<WelfareDetailFm, FmWelfareDetailBinding> {
    @Bindable
    public ObservableField<String> targetMoney = new ObservableField<>();
    @Bindable
    public ObservableField<String> money = new ObservableField<>();
    @Bindable
    public ObservableInt donateCount = new ObservableInt();
    @Bindable
    public ObservableInt percent = new ObservableInt();
    @Bindable
    public ObservableField<String> payMoney = new ObservableField<>();
    @Bindable
    public ObservableField<String> coverUrl = new ObservableField<>();
    @Bindable
    public ObservableField<String> title = new ObservableField<>();
    public View.OnClickListener clickListener = v -> toPay();

    public WelfareDetailViewModel(WelfareDetailFm welfareDetailFm, FmWelfareDetailBinding fmWelfareDetailBinding) {
        super(welfareDetailFm, fmWelfareDetailBinding);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (key == 1) {
            WelfareBean bean = (WelfareBean) object;
            if (bean == null) {
                return;
            }
            targetMoney.set(NumberFormat.getNumberInstance().format(bean.getTargetMoney()));
            money.set(NumberFormat.getNumberInstance().format(bean.getMoney()));
            percent.set(bean.getPercent());
            coverUrl.set(bean.getPicture());
            title.set(bean.getTitle());
            donateCount.set(bean.getDonateCount());
        }
    }

    private void toPay() {
        String string = payMoney.get();
        if (TextUtils.isEmpty(string)) {
            IToast.show("请输入金额");
            return;
        }
        Double aDouble = null;
        try {
            aDouble = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (aDouble == null) {
            IToast.show("请输入金额");
            return;
        }

        BigDecimal bigDecimal = BigDecimal.valueOf(aDouble);
        if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ONE) < 0) {
            IToast.show("金额最低一元");
            return;
        }
        onSkip.put(2, bigDecimal);
    }
}
