package com.lawyer.controller.cases.vm;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.cases.CasesDetailFm;
import com.lawyer.databinding.FmCaseDetailBinding;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.enums.AccountTypeEnum;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.net.Url;
import com.lawyer.util.UserCache;

/**
 @author wang
 on 2019/2/28 */

public class CasesDetailViewModel extends AbsViewModel<CasesDetailFm, FmCaseDetailBinding> {
    @Bindable
    public UserCaseBean caseBean;
    public String webUrl = "";
    //是否可投标
    @Bindable
    public ObservableBoolean isBiddingEnable = new ObservableBoolean(false);
    //律师信息是否展示
    @Bindable
    public ObservableBoolean isLawyerLayoutEnable = new ObservableBoolean(false);
    public View.OnClickListener toPayClick = v -> onSkip.put(2, true);
    public View.OnClickListener biddingClick = v -> onSkip.put(3, true);

    public CasesDetailViewModel(CasesDetailFm casesDetailFm, FmCaseDetailBinding fmCaseDetailBinding) {
        super(casesDetailFm, fmCaseDetailBinding);
    }

    @Override
    protected void putSkip(int key, Object object) {
        if (key == 11) {
            caseBean = (UserCaseBean) object;
            if (caseBean == null) return;
        }
        webUrl = Url.h5CaseDetail(caseBean.getId());
        notifyPropertyChanged(BR.vm);

        isLawyerLayoutEnable.set(caseBean.getState() != CaseStateEnum.tendering);
        notifyPropertyChanged(BR.isLawyerLayoutEnable);

        isBiddingEnable.set(caseBean.getBiddingEnable()
                && UserCache.get() != null
                && UserCache.get().getAccountType() == AccountTypeEnum.lawyer);
        notifyPropertyChanged(BR.isBiddingEnable);
        notifyPropertyChanged(BR.caseBean);

    }

}
