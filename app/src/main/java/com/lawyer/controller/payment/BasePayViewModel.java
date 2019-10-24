package com.lawyer.controller.payment;

import android.app.Activity;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lawyer.BR;
import com.lawyer.base.AbsViewModel;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.entity.WxPayBean;
import com.lawyer.enums.PayTypeEnum;
import com.lawyer.mvvm.vm.VMSupportFragment;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.LiveEventBus;
import com.lawyer.util.UserCache;
import com.lianlian.base.OnResultListener;
import com.lianlian.securepay.token.SecurePayService;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

import ink.itwo.common.util.ILog;
import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 Created by wangtaian on 2019/4/8. */
public class BasePayViewModel<FM extends VMSupportFragment, VD extends ViewDataBinding> extends AbsViewModel<FM, VD> {
    @Bindable
    public OrderCreateBean orderCreateBean;
    public View.OnClickListener aliClick = v -> {
        orderCreateBean.setPayType(PayTypeEnum.alipay);
        notifyPropertyChanged(BR.orderCreateBean);
    };
    public View.OnClickListener wechatClick = v -> {
        orderCreateBean.setPayType(PayTypeEnum.wechatpayApp);
        notifyPropertyChanged(BR.orderCreateBean);
    };
    public View.OnClickListener balanceClick = v -> {
        orderCreateBean.setPayType(PayTypeEnum.balance);
        notifyPropertyChanged(BR.orderCreateBean);
    };
    public View.OnClickListener unionClick = v -> {
        orderCreateBean.setPayType(PayTypeEnum.unionpay);
        notifyPropertyChanged(BR.orderCreateBean);
    };
    public View.OnClickListener cardClick = v -> {
        orderCreateBean.setPayType(PayTypeEnum.lianlianpay);
        notifyPropertyChanged(BR.orderCreateBean);
    };

    public BasePayViewModel(FM fm, VD vd) {
        super(fm, vd);
        LiveEventBus.get().with(PaymentFm.WECHAT_PAY_RESULT, Boolean.class)
                .observe(getView(), aBoolean -> {
                    IToast.show(aBoolean ? "支付成功" : "支付失败");
                    if (aBoolean) {
                        onSkip.put(2, orderCreateBean);
                    } else {
                        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
                    }
                });
    }

    protected void commit() {
        if (orderCreateBean == null || orderCreateBean.getMoney() == null) {
            IToast.show("支付信息错误");
            return;
        }
        if (orderCreateBean.getPayType() == null) {
            IToast.show("请选择支付方式");
            return;
        }
        if (orderCreateBean.getPayType() == PayTypeEnum.unionpay) {
            //畅捷支付
            onSkip.put(3, orderCreateBean);
            return;
        }
        if (orderCreateBean.getPayType() == PayTypeEnum.balance) {
            balancePay();
            return;
        }
        NetManager.http().create(API.class)
                .orderCreate(orderCreateBean.getOrderCreatePath(), orderCreateBean.toMap())
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        JsonElement retCodeJsonElement = result.get("retCode");
                        JsonElement errorMsgJsonElement = result.get("errorMsg");
                        JsonElement dataJsonElement = result.get("data");
                        if (retCodeJsonElement == null) {
                            return;
                        }
                        int asInt = retCodeJsonElement.getAsInt();
                        if (asInt != 0) {
                            if (errorMsgJsonElement != null) {
                                IToast.show(errorMsgJsonElement.getAsString());
                            }
                            return;
                        }
                        if (orderCreateBean.getPayType() == PayTypeEnum.alipay) {
                            ali(getView().getActivity(), dataJsonElement.getAsString());
                            return;
                        }
                        if (orderCreateBean.getPayType() == PayTypeEnum.wechatpayApp) {
                            WxPayBean wxPayBean = new Gson().fromJson(dataJsonElement, WxPayBean.class);
                            weChatPay(wxPayBean);
                            return;
                        }
                        if (orderCreateBean.getPayType() == PayTypeEnum.lianlianpay){
                            lianLianPay(getView().getActivity(),dataJsonElement.getAsString());
                            return;
                        }
                    }
                });
    }

    private void ali(final Activity context, final String data) {
        Observable.just(1)
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Integer integer) throws Exception {
                        PayTask payTask = new PayTask(context);
                        Map<String, String> payResult = payTask.payV2(data, true);
                        PayResult result = new PayResult(payResult);
                        // 同步返回需要验证的信息
                        String resultInfo = result.getResult();
                        String resultStatus = result.getResultStatus();
                        return Observable.just(TextUtils.equals(resultStatus, "9000"));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getView().addDisposableLife(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        IToast.show(aBoolean ? "支付成功" : "支付失败");
                        if (aBoolean) {
                            onSkip.put(2, orderCreateBean);
                        } else {
                            LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ILog.d(throwable.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void weChatPay(WxPayBean chatBean) {
        if (chatBean == null) {
            IToast.show("微信支付信息错误");
            return;
        }
        orderCreateBean.setOrderNo(chatBean.getOrderSn());
        IWXAPI wxapi = WXAPIFactory.createWXAPI(getView().getActivity(), chatBean.getAppid());
        if (!wxapi.isWXAppInstalled()) {
            IToast.show("未安装微信");
            return;
        }
        if (!wxapi.isWXAppSupportAPI()) {
            IToast.show("当前微信版本不支持支付");
            return;
        }

        PayReq payReq = new PayReq();
        payReq.appId = chatBean.getAppid();
        payReq.partnerId = chatBean.getPartnerid();
        payReq.prepayId = chatBean.getPrepayid();
        payReq.nonceStr = chatBean.getNoncestr();
        payReq.timeStamp = chatBean.getTimestamp();
        payReq.packageValue = chatBean.getPackageStr();
        payReq.sign = chatBean.getSign();
//        payReq.extData = "支付"; // optional
//        wxapi.registerApp(chatBean.getAppid());
        boolean b = wxapi.sendReq(payReq);
        ILog.d(b);
    }

    private void lianLianPay(final Activity context, final String data){

        //取出后台的支付url
        //0测试 1正式
        SecurePayService.securePay(context, "1111111", 0, jsonObject -> {
            //判断支付结果
            //成功
            orderCreateBean.setOrderNo("订单编号");
            onSkip.put(2, orderCreateBean);


            //


            //失败
            LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
            Log.e("SecurePayService",jsonObject.toString());
        }, false);

    }

    //余额支付
    private void balancePay() {
        if (orderCreateBean == null) return;
        Observable<Result<String>> payInfo = getBalancePayInfo();
        if (payInfo == null) return;
        payInfo
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<String>>() {
                    @Override
                    public void onNext(Result<String> result) {
                        IToast.show("支付成功");
                        orderCreateBean.setOrderNo(result.getData());
                        onSkip.put(2, orderCreateBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LiveEventBus.get().with(PaymentResultFm.BUS_PAYMENT).setValue(false);
                    }
                });
    }

    private Observable<Result<String>> getBalancePayInfo() {
        if (orderCreateBean.getType() == PaymentFm.type_case) {
            return NetManager.http().create(API.class)
                    .payCaseByCapital(UserCache.getAccessToken(), orderCreateBean.getCaseId());
        }
        if (orderCreateBean.getType() == PaymentFm.type_welfare || orderCreateBean.getType() == PaymentFm.type_member) {
            return NetManager.http().create(API.class)
                    .payProjectByCapital(UserCache.getAccessToken(), orderCreateBean.getProjectId(), orderCreateBean.getMoney());
        }
        return null;
    }

    public OrderCreateBean getOrderCreateBean() {
        return orderCreateBean;
    }
}
