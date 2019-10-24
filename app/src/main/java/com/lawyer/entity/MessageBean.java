package com.lawyer.entity;

import ink.itwo.common.util.DateUtil;

/**
 @author wang
 on 2019/3/18 */

public class MessageBean {

    /**
     content : 您的案件[     					是非得失     				]已经生成
     createTime : 1551863371000
     id : 17
     isRead : 0
     msgType : caseCreate
     updateTime : 1551863371000
     userId : 58
     */

    private String content;
    private long createTime;
    private int id;
    private int isRead;
    private String msgType;
    private long updateTime;
    private int userId;
    private String timeStr;

    public String getTimeStr() {
        return ink.itwo.common.util.DateUtil.format(createTime, DateUtil.DATE_PATTERN);
    }

    public MessageBean setTimeStr(String timeStr) {
        this.timeStr = timeStr;
        return this;
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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
