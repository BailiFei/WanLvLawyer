package com.lawyer.mvvm.vm;

/**
 Created by wang on 2018/5/9. */
public interface ISupportViewModel {
    /** View的界面创建时回调 */
    void onCreate();

    /**  View的界面销毁时回调*/
    void onDestroy();

}
