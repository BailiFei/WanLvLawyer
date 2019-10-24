package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 Created by wangtaian on 2019-05-31. */
public class DistrictBean implements Parcelable {
    private String name;
    private int parentId;
    private int sort;
    private int state;
    private String firstLetter;
    private int id;
    private long createTime;
    private long updateTime;


    public String getName() {
        return name;
    }

    public DistrictBean setName(String name) {
        this.name = name;
        return this;
    }

    public int getParentId() {
        return parentId;
    }

    public DistrictBean setParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public DistrictBean setSort(int sort) {
        this.sort = sort;
        return this;
    }

    public int getState() {
        return state;
    }

    public DistrictBean setState(int state) {
        this.state = state;
        return this;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public DistrictBean setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
        return this;
    }

    public int getId() {
        return id;
    }

    public DistrictBean setId(int id) {
        this.id = id;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public DistrictBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public DistrictBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.parentId);
        dest.writeInt(this.sort);
        dest.writeInt(this.state);
        dest.writeString(this.firstLetter);
        dest.writeInt(this.id);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
    }

    public DistrictBean() {
    }

    protected DistrictBean(Parcel in) {
        this.name = in.readString();
        this.parentId = in.readInt();
        this.sort = in.readInt();
        this.state = in.readInt();
        this.firstLetter = in.readString();
        this.id = in.readInt();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
    }

    public static final Parcelable.Creator<DistrictBean> CREATOR = new Parcelable.Creator<DistrictBean>() {
        @Override
        public DistrictBean createFromParcel(Parcel source) {
            return new DistrictBean(source);
        }

        @Override
        public DistrictBean[] newArray(int size) {
            return new DistrictBean[size];
        }
    };
}
