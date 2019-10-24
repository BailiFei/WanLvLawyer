package com.wanlvonline.lawfirm.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lawyer.R;
import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.util.LiveEventBus;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import ink.itwo.common.util.ILog;
import ink.itwo.common.util.JSONUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

//    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api = WXAPIFactory.createWXAPI(this, "wxabdf894faaca27c1");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        ILog.d(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        ILog.d(JSONUtils.toJson(resp));
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            LiveEventBus.get().with(PaymentFm.WECHAT_PAY_RESULT).setValue(resp.errCode == 0);
            finish();
        }
    }
}