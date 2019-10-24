package com.lawyer.entity;

import com.lawyer.enums.PayStateEnum;

import java.math.BigDecimal;

/**
 @author wang
 on 2019/2/28 */

public class CaseProgressBean {
    private int id;
    private int caseId;
    private int userId;
    private int lawyerId;
    private int caseType;
    private String title;
    private String picUrl;
    private BigDecimal money;
    private long createTime;
    private long updateTime;
    private String content;
    private PayStateEnum payState;

    public String getPaidStr() {
        String m = money == null ? "" : money.toString();
        return payState == PayStateEnum.paid ? "已支付" + m + "元" : "待支付" + m + "元";
    }

    public int getId() {
        return id;
    }

    public CaseProgressBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getCaseId() {
        return caseId;
    }

    public CaseProgressBean setCaseId(int caseId) {
        this.caseId = caseId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public CaseProgressBean setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getLawyerId() {
        return lawyerId;
    }

    public CaseProgressBean setLawyerId(int lawyerId) {
        this.lawyerId = lawyerId;
        return this;
    }

    public int getCaseType() {
        return caseType;
    }

    public CaseProgressBean setCaseType(int caseType) {
        this.caseType = caseType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CaseProgressBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public CaseProgressBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public CaseProgressBean setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public CaseProgressBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public CaseProgressBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CaseProgressBean setContent(String content) {
        this.content = content;
        return this;
    }


    public PayStateEnum getPayState() {
        return payState;
    }

    public CaseProgressBean setPayState(PayStateEnum payState) {
        this.payState = payState;
        return this;
    }
}
