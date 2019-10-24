package com.lawyer.controller.apply;

import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.databinding.FmApplyFirstBinding;
import com.lawyer.dialog.CaseTypeDialog;
import com.lawyer.dialog.CityPickerDialog;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.entity.DistrictBean;
import com.lawyer.entity.LawyerApplyBean;

import ink.itwo.common.util.IToast;

/**
 Created by wangtaian on 2019-05-31. */
public class FirstViewModel extends AbsViewModel<ApplyFirstFm, FmApplyFirstBinding> {
    @Bindable
    public LawyerApplyBean bean = new LawyerApplyBean();

    public FirstViewModel(ApplyFirstFm fm, FmApplyFirstBinding binding) {
        super(fm, binding);
    }


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        bean.setGender(checkedId == R.id.rd_male ? "male" : "female");
    }

    public void onCommit(View view) {
        if (!bind.checkBox.isChecked()) {
            IToast.show("请阅读并同意万律网络协议");
            return;
        }
        if (TextUtils.isEmpty(bean.getName())
                || TextUtils.isEmpty(bean.getLicenseNo())
                || TextUtils.isEmpty(bean.getQqId())
                || TextUtils.isEmpty(bean.getWechatId())
                || TextUtils.isEmpty(bean.getLawfirmName())
                || bean.getCity() == null
                || bean.getProvince() == null) {
            IToast.show("请完善信息");
            return;
        }
        getView().onResult(bean);
    }

    public void cityClick(View view) {
        CityPickerDialog
                .newInstance()
                .setCallback(new CityPickerDialog.CityPickerDialogCallback() {
                    @Override
                    public void onResult(DistrictBean province, DistrictBean city) {
                        if (province != null && city != null) {
                            bean.setProvince(province.getId());
                            bean.setCity(city.getId());
                            bind.tvCity.setText(province.getName() + city.getName());
                        }
                    }
                })
                .show(getView().getActivity().getSupportFragmentManager());
    }
    public void caseTypeClick(View view) {
        if (view==null||view.getTag()==null) return;
        Object tag = view.getTag();
        CaseTypeDialog.newInstance()
                .setCallback(new CaseTypeDialog.CaseTypeDialogCallback() {
                    @Override
                    public void onResult(CaseTypeBean typeBean) {
                        if ("1".equals(tag)) {
                            bean.setSkillFirst(typeBean.getId());
                            bean.setSkillFirstName(typeBean.getName());
                        } else if ("2".equals(tag)) {
                            bean.setSkillSecond(typeBean.getId());
                            bean.setSkillSecondName(typeBean.getName());
                        } else if ("3".equals(tag)) {
                            bean.setSkillThird(typeBean.getId());
                            bean.setSkillThirdName(typeBean.getName());
                        }
                        notifyPropertyChanged(BR.bean);
                    }
                })
                .show(getView().getActivity().getSupportFragmentManager());
    }
}
