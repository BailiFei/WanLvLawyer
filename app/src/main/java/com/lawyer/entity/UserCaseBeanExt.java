package com.lawyer.entity;

import android.text.TextUtils;

/**
 @author wang
 on 2019/3/16 */

public class UserCaseBeanExt extends UserCaseBean {

    /**
     caseTitle : 是的
     mobileNo : 15678781236
     name : 无名
     state : fail
     */

    private String caseTitle;
    private String mobileNo;
    private String name;
    //0:我的投标，1：进行中，2：已结束
    private int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public UserCaseBeanExt setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public String getCaseTitle() {
        if (TextUtils.isEmpty(caseTitle)) return getTitle();
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 律师页面，0:我的投标，1 2 ：进行中、已结束*/
    public String getMobileExt() {
        return currentPosition == 0 ? mobileNo : getLawyerMobileNo();
    }
    public String getTitleExt() {
        return currentPosition == 0 ? caseTitle : getTitle();
    }
}
