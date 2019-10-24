package com.lawyer.util.rxView;

import android.os.Looper;
import android.support.annotation.RestrictTo;

import io.reactivex.Observer;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 Created by wangtaian
 on 2018/6/9 */
@RestrictTo(LIBRARY_GROUP)
public final class Preconditions {
    public static void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }

    public static boolean checkMainThread(Observer<?> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onError(new IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
            return false;
        }
        return true;
    }

    private Preconditions() {
        throw new AssertionError("No instances.");
    }
}
