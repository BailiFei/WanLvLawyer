package com.lawyer.entity;

import java.math.BigDecimal;

/**
 @author wang
 on 2019/2/26 */

public class WelfareProgressBean {
    private int id;

    private int projectId;

    private String name;

    private String description;

    private BigDecimal money;

    private String picUrl;

    private long createTime;

    private long updateTime;

    public int getId() {
        return id;
    }

    public WelfareProgressBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getProjectId() {
        return projectId;
    }

    public WelfareProgressBean setProjectId(int projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public WelfareProgressBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WelfareProgressBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public WelfareProgressBean setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public WelfareProgressBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public WelfareProgressBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public WelfareProgressBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
