package com.lawyer.controller.welfare;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.mvvm.adapter.webview.ViewAdapter;

/**
 @author wang
 on 2019/2/14 */

public class WelfareWebViewChildFm extends BaseFragment<MainActivity> {
    public static WelfareWebViewChildFm newInstance(String url) {

        Bundle args = new Bundle();

        WelfareWebViewChildFm fragment = new WelfareWebViewChildFm();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    private WebView webView;


    @Override
    protected int initLayoutId() {
        return R.layout.fm_welfare_web_view_child;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        webView = findViewById(R.id.web_view);
        String url = getArguments().getString("url");
        ViewAdapter.setWebUrl(webView, url);
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
            super.onDestroy();
        }
    }
}
