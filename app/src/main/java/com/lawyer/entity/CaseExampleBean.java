package com.lawyer.entity;

/**
 @author wang
 on 2019/2/21 */

public class CaseExampleBean {

    /**
     caseType : 3
     content : 经典案例赏析
     createTime : 1548414552000
     id : 7
     recommend : 1
     state : normal
     title : 经典案例赏析
     updateTime : 1550671595000
     */

    private int caseType;
    private String content;
    private long createTime;
    private int id;
    private int recommend;
    private String state;
    private String title;
    private String picture;
    private long updateTime;

    public String getPicture() {
        return picture;
    }

    public CaseExampleBean setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public int getCaseType() {
        return caseType;
    }

    public void setCaseType(int caseType) {
        this.caseType = caseType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
