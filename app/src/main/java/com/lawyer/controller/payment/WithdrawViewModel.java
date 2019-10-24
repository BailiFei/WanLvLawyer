package com.lawyer.controller.payment;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.databinding.FmWithdrawBinding;
import com.lawyer.entity.BankCardBean;
import com.lawyer.entity.CapitalWithdrawBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import java.math.BigDecimal;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 Created by wangtaian on 2019/4/25. */
public class WithdrawViewModel extends AbsViewModel<WithdrawFm, FmWithdrawBinding> {
    @Bindable
    public ObservableField<String> amount = new ObservableField<>();
    @Bindable
    public ObservableField<String> name = new ObservableField<>();
    @Bindable
    public ObservableField<String> idCard = new ObservableField<>();
    @Bindable
    public ObservableField<String> cardNo = new ObservableField<>();
    @Bindable
    public ObservableField<String> mobile = new ObservableField<>();
    @Bindable
    public ObservableField<String> balance = new ObservableField<>();
    private BigDecimal max;

    public View.OnClickListener confirmClick = v -> confirm();
    public View.OnClickListener clickAll = v -> {
        if (max == null) max = BigDecimal.ZERO;
        amount.set(max.toString());
    };

    public WithdrawViewModel(WithdrawFm fm, FmWithdrawBinding binding) {
        super(fm, binding);
        amount.addOnPropertyChangedCallback(propertyChangedCallback);
        name.addOnPropertyChangedCallback(propertyChangedCallback);
        idCard.addOnPropertyChangedCallback(propertyChangedCallback);
        cardNo.addOnPropertyChangedCallback(propertyChangedCallback);
        mobile.addOnPropertyChangedCallback(propertyChangedCallback);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (1 == key) {
            max = (BigDecimal) object;
            if (max == null) max = BigDecimal.ZERO;
            balance.set("账户余额：" + max.toString() + "元");
            notifyPropertyChanged(BR.balance);
        }
    }

    private OnPropertyChangedCallback propertyChangedCallback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            boolean disEnable = TextUtils.isEmpty(name.get())
                    || TextUtils.isEmpty(idCard.get())
                    || TextUtils.isEmpty(cardNo.get())
                    || TextUtils.isEmpty(mobile.get())
                    || TextUtils.isEmpty(amount.get());
            bind.tvCommit.setClickable(!disEnable);
            bind.tvCommit.setBackgroundResource(disEnable ? R.drawable.bg_green_b0e4c1_radius_22 : R.drawable.bg_green_16aa47_radius_22);
        }
    };


    @Override
    public void getInfo() {
        NetManager.http().create(API.class)
                .getBankCardDetail(UserCache.getAccessToken())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<BankCardBean>>() {
                    @Override
                    public void onNext(Result<BankCardBean> result) {
                        BankCardBean bean = result.getData();
                        if (bean == null) {
                            return;
                        }
                        name.set(bean.getName());
                        idCard.set(bean.getIdCard());
                        cardNo.set(bean.getCardNo());
                        mobile.set(bean.getMobile());
                        notifyPropertyChanged(BR.name);
                        notifyPropertyChanged(BR.idCard);
                        notifyPropertyChanged(BR.cardNo);
                        notifyPropertyChanged(BR.mobile);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void confirm() {
        if (TextUtils.isEmpty(amount.get())) {
            IToast.show("请输入提现金额");
            return;
        }
        if (TextUtils.isEmpty(name.get())) {
            IToast.show("请输入提现银行卡的开户名");
            return;
        }
        if (TextUtils.isEmpty(idCard.get())) {
            IToast.show("请输入提现银行卡的对应的身份证号");
            return;
        }
        if (TextUtils.isEmpty(cardNo.get())) {
            IToast.show("请输入提现银行卡号");
            return;
        }
        if (TextUtils.isEmpty(mobile.get())) {
            IToast.show("请输入银行预留的手机号");
            return;
        }
        if (!VerifyUtil.isMobile(mobile.get())) {
            IToast.show("请输入正确手机号");
            return;
        }
        toConfirm();
    }

    private void toConfirm() {
        NetManager.http().create(API.class)
                .doBankCardAuth(UserCache.getAccessToken(), cardNo.get(), idCard.get(), mobile.get(), name.get())
                .flatMap(new Function<Result, ObservableSource<Result<CapitalWithdrawBean>>>() {
                    @Override
                    public ObservableSource<Result<CapitalWithdrawBean>> apply(Result result) throws Exception {
                        if (result.isSuccess()) {
                            return NetManager.http().create(API.class).withdraw(UserCache.getAccessToken(), amount.get());
                        }
                        IToast.show(result.getErrorMsg());
                        return io.reactivex.Observable.empty();
                    }
                })
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<CapitalWithdrawBean>>() {
                    @Override
                    public void onNext(Result<CapitalWithdrawBean> result) {
                        CapitalWithdrawBean data = result.getData();
                        if (data!=null) data.setApplyAmount(amount.get());
                        onSkip.put(11, result.getData());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

}
