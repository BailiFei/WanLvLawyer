package com.lawyer.enums;

import android.support.annotation.DrawableRes;

import com.lawyer.R;

/**
 @author wang
 on 2019/2/22 */

public enum CaseStateEnum {
    tendering("招标中"),

    solving("处理中"),//进行中（招标结束）

    done("结束"),

    delete("作废");

    private String description;

    CaseStateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getStateStr() {
        if (this == tendering) return "投标";
        if (this == solving || this == done) return "已投";
        else return "作废";
    }
    public String getTitle() {
        if (this==tendering) return "待接洽";
        if (this==solving) return "进行中";
        if (this==done) return "已结束";
        return "";
    }

    public String getLawyerTitles() {
        if (this==tendering) return "我的投标";
        if (this==solving) return "进行中";
        if (this==done) return "已结束";
        return "";
    }
    /** 律师首页案件状态背景 */
    @DrawableRes
    public int indexTagBackground() {
        if (this == tendering) return R.drawable.bg_green_16aa47_15;
        else return R.drawable.bg_green_b0e4c1_15;
    }
}
