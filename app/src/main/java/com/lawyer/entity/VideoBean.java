package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 @author wang
 on 2019/2/21 */

public class VideoBean implements Parcelable {
    private int id;
    private int caseType;
    private String title;
    private String picUrl;
    private String videoUrl;
    private int recommend;
    private int isHomepage;
    private int playCount;
    private long createTime;
    private long updateTime;
    private String caseTypeName;
    private String duration;

    public int getPlayCount() {
        return playCount;
    }

    public VideoBean setPlayCount(int playCount) {
        this.playCount = playCount;
        return this;
    }

    public int getId() {
        return id;
    }

    public VideoBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getCaseType() {
        return caseType;
    }

    public VideoBean setCaseType(int caseType) {
        this.caseType = caseType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public VideoBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public VideoBean setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public int getRecommend() {
        return recommend;
    }

    public VideoBean setRecommend(int recommend) {
        this.recommend = recommend;
        return this;
    }

    public int getIsHomepage() {
        return isHomepage;
    }

    public VideoBean setIsHomepage(int isHomepage) {
        this.isHomepage = isHomepage;
        return this;
    }


    public long getCreateTime() {
        return createTime;
    }

    public VideoBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public VideoBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public VideoBean setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public VideoBean setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public VideoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.caseType);
        dest.writeString(this.title);
        dest.writeString(this.picUrl);
        dest.writeString(this.videoUrl);
        dest.writeInt(this.recommend);
        dest.writeInt(this.isHomepage);
        dest.writeInt(this.playCount);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeString(this.caseTypeName);
        dest.writeString(this.duration);
    }

    protected VideoBean(Parcel in) {
        this.id = in.readInt();
        this.caseType = in.readInt();
        this.title = in.readString();
        this.picUrl = in.readString();
        this.videoUrl = in.readString();
        this.recommend = in.readInt();
        this.isHomepage = in.readInt();
        this.playCount = in.readInt();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.caseTypeName = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel source) {
            return new VideoBean(source);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };
}
