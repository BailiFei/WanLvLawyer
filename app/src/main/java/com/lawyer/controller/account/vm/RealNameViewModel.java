package com.lawyer.controller.account.vm;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.account.RealNameFm;
import com.lawyer.databinding.AccoutRealNameBinding;
import com.lawyer.entity.User;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import java.util.HashMap;
import java.util.Map;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/3/1 */

public class RealNameViewModel extends AbsViewModel<RealNameFm, AccoutRealNameBinding> {
    @Bindable
    public ObservableField<String> realName = new ObservableField<>();
    @Bindable
    public ObservableField<String> idNumber = new ObservableField<>();
    public View.OnClickListener onClickListener = this::onClick;

    public RealNameViewModel(RealNameFm realNameFm, AccoutRealNameBinding accoutRealNameBinding) {
        super(realNameFm, accoutRealNameBinding);
    }

    private void onClick(View v) {
        if (!VerifyUtil.isRealName(realName.get())) {
            IToast.show("请输入您的真实姓名");
            return;
        }
        if (!VerifyUtil.isIdNumber(idNumber.get())) {
            IToast.show("请输入您的身份证号");
            return;
        }
        onCommit();
    }

    private void onCommit() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("accessToken", UserCache.getAccessToken());
        userMap.put("idCardNo", idNumber.get());
        userMap.put("name", realName.get());
        NetManager.http().create(API.class)
                .userUpdateUserInfo(userMap)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<User>>() {
                    @Override
                    public void onNext(Result<User> result) {
                        if (result.getData() != null) {
                            User userInCache = UserCache.get();
                            userInCache.setName(realName.get());
                            userInCache.setIdCardNo(idNumber.get());
                            UserCache.put(userInCache);
                            onSkip.put(1, true);
                        }
                    }
                });
    }
}
