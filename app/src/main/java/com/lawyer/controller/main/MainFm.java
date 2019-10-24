package com.lawyer.controller.main;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.account.LoginFm;
import com.lawyer.enums.AccountTypeEnum;
import com.lawyer.util.UserCache;
import com.lawyer.view.ManualRadioGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 @author wang
 on 2019/1/30 */

public class MainFm extends BaseFragment<MainActivity> implements RadioGroup.OnCheckedChangeListener {
    private BaseFragment[] fragments = new BaseFragment[5];

    public static MainFm newInstance() {

        Bundle args = new Bundle();
        MainFm fragment = new MainFm();
        fragment.setArguments(args);
        return fragment;
    }

    public static final int cases = 2, video = 3, welfare = 4, user = 5;

    @IntDef({cases, video, welfare, user})
    @Retention(RetentionPolicy.SOURCE)
    public @interface perform {

    }

    private AccountTypeEnum type;
    private ManualRadioGroup radioGroup;
    @Override
    protected int initLayoutId() {
        return R.layout.fm_main;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(false);
        type = UserCache.get().getAccountType();
        //未登录过，默认是用户端
        if (type == null) type = AccountTypeEnum.user;
        findViewById(R.id.radio_bidding).setVisibility(type == AccountTypeEnum.user ? View.GONE : View.VISIBLE);
        findViewById(R.id.radio_advisory).setVisibility(type == AccountTypeEnum.user ? View.VISIBLE : View.GONE);
        fragments[0] = type == AccountTypeEnum.user ? new MainAdvisoryFm() : new MainBiddingFm();
        fragments[1] = new MainCaseFm();
        fragments[2] = new MainVideoFm();
        fragments[3] = new MainWelfareFm();
        fragments[4] = type == AccountTypeEnum.user ? new MainUserFm() : new MainLawyerFm();
        loadMultipleRootFragment(R.id.frame_layout,0, fragments);
        radioGroup = viewRoot.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.setInterceptListener(isIntercept -> {
            if (isIntercept) {
                LoginFm loginFm = LoginFm.newInstance();
                loginFm.setResultListener(isSuccess -> {
                    if (isSuccess) {
                        onPerformClick(user);
                    }
                });
                mActivity.start(loginFm);
            }
        });
        ((RadioButton) viewRoot.findViewById(type == AccountTypeEnum.user?R.id.radio_advisory:R.id.radio_bidding)).performClick();
    }

    public void onPerformClick(@perform int perform) {
        if (radioGroup == null) return;
        radioGroup.getChildAt(perform).performClick();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_advisory||checkedId==R.id.radio_bidding) {
            showHideFragment(fragments[0]);
        } else if (checkedId == R.id.radio_case) {
            showHideFragment(fragments[1]);
        } else if (checkedId == R.id.radio_video) {
            showHideFragment(fragments[2]);
        } else if (checkedId == R.id.radio_welfare) {
//            showHideFragment(fragments[3]);
        } else if (checkedId == R.id.radio_user) {
            if (!UserCache.isLogged()) {
                return;
            }
            showHideFragment(fragments[4]);
        }
    }
}
