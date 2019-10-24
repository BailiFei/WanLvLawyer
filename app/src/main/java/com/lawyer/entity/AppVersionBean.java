package com.lawyer.entity;

/**
 @author wang
 on 2019/3/4 */

public class AppVersionBean {
    private int id;
    private String versionnum;
    private String description;
    private String updateUrl;
    //是否强制 1是0否
    private int compulsory;
    private String device;
    private long createTime;
    private long updateTime;

    public int getId() {
        return id;
    }

    public AppVersionBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getVersionnum() {
        return versionnum;
    }

    public AppVersionBean setVersionnum(String versionnum) {
        this.versionnum = versionnum;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AppVersionBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public AppVersionBean setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
        return this;
    }

    public int getCompulsory() {
        return compulsory;
    }

    public AppVersionBean setCompulsory(int compulsory) {
        this.compulsory = compulsory;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public AppVersionBean setDevice(String device) {
        this.device = device;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public AppVersionBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public AppVersionBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
