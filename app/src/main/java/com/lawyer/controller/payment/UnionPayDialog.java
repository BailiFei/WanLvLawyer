package com.lawyer.controller.payment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.entity.OrderCreateBean;
import com.lawyer.entity.UnionBean;
import com.lawyer.enums.PayStateEnum;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import java.util.concurrent.TimeUnit;

import ink.itwo.common.ctrl.CommonDialog;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.util.IToast;
import ink.itwo.common.util.JSONUtils;
import ink.itwo.common.widget.CountDownTimerView;
import ink.itwo.net.NetManager;
import ink.itwo.net.dialog.DialogUtils;
import ink.itwo.net.life.RxLifecycle;
import ink.itwo.net.transformer.NetTransformer;
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 畅捷支付-银联支付 验证码校验
 Created by wangtaian on 2019-06-06. */
public class UnionPayDialog extends CommonDialog<MainActivity> {
    public static UnionPayDialog newInstance(OrderCreateBean orderCreateBean) {

        Bundle args = new Bundle();

        UnionPayDialog fragment = new UnionPayDialog();
        args.putParcelable("orderCreateBean", orderCreateBean);
        fragment.setArguments(args);
        return fragment;
    }

    public interface UnionPayDialogCallback {
        void onResult(boolean success, OrderCreateBean orderCreateBean);
    }

    private UnionPayDialogCallback callback;

    public void setCallback(UnionPayDialogCallback callback) {
        this.callback = callback;
    }

    private UnionBean unionBean;
    private OrderCreateBean orderCreateBean;
    private TextView tvCommit;
    private EditText editText;
    private ImageView ivClose, ivClear;
    private CountDownTimerView countDownTimerView;
    private boolean isCodeFirst = true;

    @Override
    public int intLayoutId() {
        return R.layout.dialog_union_pay_check_code;
    }

    @Override
    public void convertView(View view) {
        setWidth(MATCH_PARENT).setHeight(DeviceUtil.getDimensionPx(R.dimen.dp_450)).setOutCancel(false);
        orderCreateBean = getArguments().getParcelable("orderCreateBean");

        tvCommit = view.findViewById(R.id.tv_commit);
        editText = view.findViewById(R.id.edit_text);
        ivClose = view.findViewById(R.id.iv_close);
        ivClear = view.findViewById(R.id.iv_clear);
        countDownTimerView = view.findViewById(R.id.count_down_view);

        countDownTimerView.setMobile(orderCreateBean.getMobNo());
        countDownTimerView.setListener(new CountDownTimerView.CountDownTimerViewListener() {
            @Override
            public void onStart() {
                if (isCodeFirst) {
                    getCode();
                } else {
                    getCodeAgain();
                }

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        });
        ivClear.setOnClickListener(v -> editText.setText(""));
        ivClear.setVisibility(View.INVISIBLE);
        tvCommit.setOnClickListener(this::commit);
        ivClose.setOnClickListener(v -> dismissAllowingStateLoss());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ivClear.setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private Disposable disposable;
    DialogUtils dialogUtils = new DialogUtils();

    private void commit(View view) {
        Editable text = editText.getText();
        if (text == null) {
            IToast.show("请输入验证码");
            return;
        }
        if (unionBean == null) {
            IToast.show("信息错误");
            return;
        }
        dialogUtils.showProgress(mActivity);
        String code = text.toString();
        NetManager.http().create(API.class)
                .orderSMSConfirm(unionBean.getTrxId(), code)
                .compose(RxLifecycle.bind(this))
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Result, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Result result) throws Exception {
                        return Observable.interval(1, 3, TimeUnit.SECONDS);
                    }
                })
                .flatMap(new Function<Long, ObservableSource<Result<PayStateEnum>>>() {
                    @Override
                    public ObservableSource<Result<PayStateEnum>> apply(Long aLong) throws Exception {
                        return NetManager.http().create(API.class)
                                .orderState(UserCache.getAccessToken(), unionBean.getTrxId());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(ProgressDialogUtils.applyProgressBar(this))
                .subscribe(new ResultObserver<Result<PayStateEnum>>() {
                    @Override
                    public void onNext(Result<PayStateEnum> result) {
                        PayStateEnum stateEnum = result.getData();
                        if (stateEnum == PayStateEnum.paid) {
                            if (callback != null) {
                                callback.onResult(true, orderCreateBean);
                            }
                            if (disposable != null) disposable.dispose();
                            if (dialogUtils != null) dialogUtils.dismissProgress();
                            dismissAllowingStateLoss();
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        disposable = d;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (disposable != null) disposable.dispose();
                        if (dialogUtils != null) dialogUtils.dismissProgress();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (dialogUtils != null) dialogUtils.dismissProgress();
                    }
                });
    }

    private void getCode() {
        if (orderCreateBean == null) {
            IToast.show("信息错误");
            return;
        }
        if (TextUtils.isEmpty(orderCreateBean.getBkAcctNo())
                || TextUtils.isEmpty(orderCreateBean.getIDNo())
                || TextUtils.isEmpty(orderCreateBean.getCstmrNm())
                || !VerifyUtil.isMobile(orderCreateBean.getMobNo())) {
            IToast.show("信息输入不完整");
            return;
        }
        NetManager.http().create(API.class)
                .orderCreate(orderCreateBean.getOrderCreatePath(), orderCreateBean.toMap())
                .compose(NetTransformer.handle(this))
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
                        String string = dataJsonElement.getAsString();
                        unionBean = JSONUtils.getObj(string, UnionBean.class);
                        if (unionBean == null) {
                            IToast.show("信息错误");
                            return;
                        }
                        if (!"S".equals(unionBean.getAcceptStatus())) {
                            IToast.show(unionBean.getRetMsg());
                            return;
                        }
                        orderCreateBean.setOrderNo(unionBean.getTrxId());
                        IToast.show("验证码已发送");
                        isCodeFirst = false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    private void getCodeAgain() {
        if (unionBean == null) {
            IToast.show("信息错误");
            return;
        }
        NetManager.http().create(API.class)
                .orderSMSResend(unionBean.getTrxId())
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<String>>() {
                    @Override
                    public void onNext(Result<String> result) {
                        String data = result.getData();
                        if (data != null) {
                            IToast.show(data);
                        }
                    }

                });
    }
}
