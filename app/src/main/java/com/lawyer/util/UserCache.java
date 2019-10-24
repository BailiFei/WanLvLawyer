package com.lawyer.util;

import android.text.TextUtils;

import com.lawyer.entity.User;

import ink.itwo.common.util.CacheUtil;

/**
 @author wang
 on 2019/2/21 */

public class UserCache {
    public static final String KEY_CACHE_USER = "user_cache_190221";

    public static boolean put(User user) {
        return CacheUtil.put(KEY_CACHE_USER, user);
    }

    public static User get() {
        User user = CacheUtil.getObject(KEY_CACHE_USER, User.class);
        if (user == null) user = new User();
        return user;
    }
    public static boolean clear() {
        return CacheUtil.remove(KEY_CACHE_USER);
    }

    public static String getAccessToken() {
        return get().getAccessToken();
    }
    /** true : 已登录*/
    public static boolean isLogged() {
        return !TextUtils.isEmpty(getAccessToken());
    }
}
