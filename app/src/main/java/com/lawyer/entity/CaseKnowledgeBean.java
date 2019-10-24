package com.lawyer.entity;

import ink.itwo.common.util.DateUtil;

/**
 @author wang
 on 2019/2/21 */

public class CaseKnowledgeBean {

    private int id;

    private int caseType;

    private int knowledgeType;

    private String title;

    private String picUrl;


    private int recommend;

    private int readcount;

    private long createTime;

    private long updateTime;

    private String content;

    private String caseTypeName;

    private String knowledgeTypeName;
    private boolean isHeader;

    public String getDateStr() {
        return DateUtil.format(createTime, "yyyy.MM.dd");
    }

    public int getId() {
        return id;
    }

    public CaseKnowledgeBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getCaseType() {
        return caseType;
    }

    public CaseKnowledgeBean setCaseType(int caseType) {
        this.caseType = caseType;
        return this;
    }

    public int getKnowledgeType() {
        return knowledgeType;
    }

    public CaseKnowledgeBean setKnowledgeType(int knowledgeType) {
        this.knowledgeType = knowledgeType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CaseKnowledgeBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public CaseKnowledgeBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public int getRecommend() {
        return recommend;
    }

    public CaseKnowledgeBean setRecommend(int recommend) {
        this.recommend = recommend;
        return this;
    }

    public int getReadcount() {
        return readcount;
    }

    public CaseKnowledgeBean setReadcount(int readcount) {
        this.readcount = readcount;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public CaseKnowledgeBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public CaseKnowledgeBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CaseKnowledgeBean setContent(String content) {
        this.content = content;
        return this;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public CaseKnowledgeBean setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
        return this;
    }

    public String getKnowledgeTypeName() {
        return knowledgeTypeName;
    }

    public CaseKnowledgeBean setKnowledgeTypeName(String knowledgeTypeName) {
        this.knowledgeTypeName = knowledgeTypeName;
        return this;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public CaseKnowledgeBean setHeader(boolean header) {
        isHeader = header;
        return this;
    }
}
