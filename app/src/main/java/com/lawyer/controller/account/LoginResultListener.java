package com.lawyer.controller.account;

/**
 是否成功登录
 @author wang
 on 2019/3/11 */
@FunctionalInterface
public interface LoginResultListener {
    /** 是否成功登录 */
    void onLoginResult(boolean isSuccess);
}
