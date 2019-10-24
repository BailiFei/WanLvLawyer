package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 @author wang
 on 2019/2/21 */

public class CaseTypeBean implements Parcelable {

    /**
     icon : bb.png
     id : 1
     isDelete : 0
     name : 婚姻家庭
     */

    private String icon;
    private int id;
    private int isDelete;
    private String name;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeInt(this.id);
        dest.writeInt(this.isDelete);
        dest.writeString(this.name);
    }

    public CaseTypeBean() {
    }

    protected CaseTypeBean(Parcel in) {
        this.icon = in.readString();
        this.id = in.readInt();
        this.isDelete = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CaseTypeBean> CREATOR = new Parcelable.Creator<CaseTypeBean>() {
        @Override
        public CaseTypeBean createFromParcel(Parcel source) {
            return new CaseTypeBean(source);
        }

        @Override
        public CaseTypeBean[] newArray(int size) {
            return new CaseTypeBean[size];
        }
    };
}
