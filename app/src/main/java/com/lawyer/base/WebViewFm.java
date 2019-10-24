package com.lawyer.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.mvvm.adapter.webview.ViewAdapter;

/**
 @author wang
 on 2019/2/13 */

public class WebViewFm extends BaseFragment<MainActivity> {
    public static WebViewFm newInstance(String url,String title) {

        Bundle args = new Bundle();

        WebViewFm fragment = new WebViewFm();
        args.putString("url", url);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    private WebView webView;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_common_web_view;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(true);
        String title = getArguments().getString("title");
        setTitle(title);
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
