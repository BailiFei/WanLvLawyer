package com.lawyer.entity;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 @author wang
 on 2019/2/26 */

public class WelfareRecordBean {
    private int id;

    private int projectId;

    private int userId;

    private String mobileNo;

    private BigDecimal money;

    private String remark;

    private long createTime;

    private long updateTime;

    private int hourDiff;

    private int dayDiff;

    public String getTimeText() {
        if (dayDiff != 0) return dayDiff + "天前";
        return hourDiff + "小时前";
    }

    public String getMoneyText() {
        return (money == null ? "" : money.toString()) + "元";
    }

    public String getMobileText() {
        if (TextUtils.isEmpty(mobileNo) || mobileNo.length() != 11) return mobileNo;
        return mobileNo.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public int getId() {
        return id;
    }

    public WelfareRecordBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getProjectId() {
        return projectId;
    }

    public WelfareRecordBean setProjectId(int projectId) {
        this.projectId = projectId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public WelfareRecordBean setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public WelfareRecordBean setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public WelfareRecordBean setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public WelfareRecordBean setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public WelfareRecordBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public WelfareRecordBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public int getHourDiff() {
        return hourDiff;
    }

    public WelfareRecordBean setHourDiff(int hourDiff) {
        this.hourDiff = hourDiff;
        return this;
    }

    public int getDayDiff() {
        return dayDiff;
    }

    public WelfareRecordBean setDayDiff(int dayDiff) {
        this.dayDiff = dayDiff;
        return this;
    }
}
