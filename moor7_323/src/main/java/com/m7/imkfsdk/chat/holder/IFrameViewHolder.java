package com.m7.imkfsdk.chat.holder;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.m7.imkfsdk.R;

/**
 * Created by longwei on 2016/3/10.
 */
public class IFrameViewHolder extends BaseHolder {

    private WebView mWebView;

    public IFrameViewHolder(int type) {
        super(type);
    }

    public BaseHolder initBaseHolder(View baseView, boolean isReceive) {
        super.initBaseHolder(baseView);

        //通过baseview找到对应组件
        mWebView = (WebView) baseView.findViewById(R.id.chat_webview);
        if(isReceive) {
            type = 10;
            return this;
        }
        return this;
    }

    public WebView getWebView() {
        if(mWebView == null) {
            mWebView = (WebView) getBaseView().findViewById(R.id.chat_webview);
        }
        return mWebView;
    }
}
