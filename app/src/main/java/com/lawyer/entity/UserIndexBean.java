package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/21 */

public class UserIndexBean {
    private User user;
    private List<Banner> banner;
    private List<CaseExampleBean> caseExampleList;
    private List<VideoBean> caseVideoList;
    private List<CaseKnowledgeBean> caseKnowledgeList;
    private List<CaseTypeBean> caseTypeList;
    private String telNum;

    public String getTelNum() {
        return telNum;
    }

    public UserIndexBean setTelNum(String telNum) {
        this.telNum = telNum;
        return this;
    }

    public List<CaseTypeBean> getCaseTypeList() {
        return caseTypeList;
    }

    public UserIndexBean setCaseTypeList(List<CaseTypeBean> caseTypeList) {
        this.caseTypeList = caseTypeList;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserIndexBean setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public UserIndexBean setBanner(List<Banner> banner) {
        this.banner = banner;
        return this;
    }

    public List<CaseExampleBean> getCaseExampleList() {
        return caseExampleList;
    }

    public UserIndexBean setCaseExampleList(List<CaseExampleBean> caseExampleList) {
        this.caseExampleList = caseExampleList;
        return this;
    }

    public List<VideoBean> getCaseVideoList() {
        return caseVideoList;
    }

    public UserIndexBean setCaseVideoList(List<VideoBean> caseVideoList) {
        this.caseVideoList = caseVideoList;
        return this;
    }

    public List<CaseKnowledgeBean> getCaseKnowledgeList() {
        return caseKnowledgeList;
    }

    public UserIndexBean setCaseKnowledgeList(List<CaseKnowledgeBean> caseKnowledgeList) {
        this.caseKnowledgeList = caseKnowledgeList;
        return this;
    }
}
