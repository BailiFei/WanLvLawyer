package com.lawyer.entity;

import com.lawyer.enums.BannerTypeEnum;

import ink.itwo.common.widget.imgRes.ImageResource;

/**
 @author wang
 on 2019/2/21 */

public class Banner implements ImageResource {

    /**
     createTime : 1548926262000
     desc : banner33
     id : 7
     link : banner33
     pic : banner33
     state : deleted
     title : banner33
     type : app
     updateTime : 1550668023000
     */

    private long createTime;
    private String desc;
    private int id;
    private String link;
    private String pic;
    private String state;
    private String title;
    private BannerTypeEnum type;
    private long updateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public BannerTypeEnum getType() {
        return type;
    }

    public Banner setType(BannerTypeEnum type) {
        this.type = type;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Object thumbnailUrl() {
        return pic;
    }
}
