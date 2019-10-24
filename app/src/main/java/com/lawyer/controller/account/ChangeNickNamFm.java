package com.lawyer.controller.account;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.entity.User;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.lawyer.util.VerifyUtil;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/** 修改用户昵称
 @author wang
 on 2019/2/20 */

public class ChangeNickNamFm extends BaseFragment<MainActivity> {
    public static ChangeNickNamFm newInstance() {

        Bundle args = new Bundle();

        ChangeNickNamFm fragment = new ChangeNickNamFm();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int initLayoutId() {
        return R.layout.fm_change_name;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("修改昵称");
        EditText editText = findViewById(R.id.edit);
        String nickName = UserCache.get().getNickName();
        if (TextUtils.isEmpty(nickName)) nickName = "";
        editText.setText(nickName);
        findViewById(R.id.tv_commit)
                .setOnClickListener(v -> {
                    Editable text = editText.getText();
                    if (!VerifyUtil.isNickName(text.toString())) {
                        IToast.show("请输入您的昵称");
                        return;
                    }
                    toCommit(text.toString());
                });
    }

    private void toCommit(String nickName) {
        NetManager.http().create(API.class)
                .userUpdateUserInfo(UserCache.getAccessToken(), nickName)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<User>>() {
                    @Override
                    public void onNext(Result<User> result) {
                        User data = result.getData();
                        UserCache.put(data);
                        IToast.show("昵称已修改");
                        Bundle bundle = new Bundle();
                        bundle.putString("nickName", nickName);
                        setFragmentResult(RESULT_OK, bundle);
                        pop();
                    }
                });
    }
}
