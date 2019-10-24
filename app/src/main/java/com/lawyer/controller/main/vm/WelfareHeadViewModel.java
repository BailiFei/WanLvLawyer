package com.lawyer.controller.main.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

/**
 @author wang
 on 2019/2/26 */

public class WelfareHeadViewModel extends BaseObservable {
    @Bindable
    public ObservableField<String> totalMoney = new ObservableField<>();
    @Bindable
    public ObservableField<String> totalHelpPeole = new ObservableField<>();
    @Bindable
    public ObservableField<String> totalHelpCount = new ObservableField<>();
}
