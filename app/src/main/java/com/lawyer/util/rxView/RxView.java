package com.lawyer.util.rxView;

import android.view.View;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static com.lawyer.util.rxView.Preconditions.checkNotNull;


/**
 * Created by wangtaian
 * on 2018/6/9
 */
public class RxView {
    public static Observable<Object> clicks(@NonNull View view) {
        checkNotNull(view, "view == null");
        return new ViewClickObservable(view);
    }
}
