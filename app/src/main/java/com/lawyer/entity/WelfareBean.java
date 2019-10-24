package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lawyer.enums.WelfareStateEnum;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 @author wang
 on 2019/2/26 */

public class WelfareBean implements Parcelable {
    private Integer id;

    private String title;

    private String type;

    private String picture;

    private WelfareStateEnum state;

    private BigDecimal targetMoney;

    private BigDecimal money = BigDecimal.ZERO;

    private String content;
    private int donateCount;

    public String getTargetMoneyStr() {
        return NumberFormat.getNumberInstance().format(targetMoney == null ? BigDecimal.ZERO : targetMoney);
    }

    public String getMoneyStr() {
        return NumberFormat.getNumberInstance().format(money);
    }

    public int getDonateCount() {
        return donateCount;
    }

    public WelfareBean setDonateCount(int donateCount) {
        this.donateCount = donateCount;
        return this;
    }

    public int getPercent() {
        if (targetMoney == null || money == null || BigDecimal.ZERO.equals(targetMoney)) {
            return 0;
        }
        return (int) (100 * money.doubleValue() / targetMoney.doubleValue());
    }

    public Integer getId() {
        return id;
    }

    public WelfareBean setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public WelfareBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getType() {
        return type;
    }

    public WelfareBean setType(String type) {
        this.type = type;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public WelfareBean setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public WelfareStateEnum getState() {
        return state;
    }

    public WelfareBean setState(WelfareStateEnum state) {
        this.state = state;
        return this;
    }

    public BigDecimal getTargetMoney() {
        return targetMoney;
    }

    public WelfareBean setTargetMoney(BigDecimal targetMoney) {
        this.targetMoney = targetMoney;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public WelfareBean setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getContent() {
        return content;
    }

    public WelfareBean setContent(String content) {
        this.content = content;
        return this;
    }

    public WelfareBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.picture);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeSerializable(this.targetMoney);
        dest.writeSerializable(this.money);
        dest.writeString(this.content);
        dest.writeInt(this.donateCount);
    }

    protected WelfareBean(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.type = in.readString();
        this.picture = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : WelfareStateEnum.values()[tmpState];
        this.targetMoney = (BigDecimal) in.readSerializable();
        this.money = (BigDecimal) in.readSerializable();
        this.content = in.readString();
        this.donateCount = in.readInt();
    }

    public static final Creator<WelfareBean> CREATOR = new Creator<WelfareBean>() {
        @Override
        public WelfareBean createFromParcel(Parcel source) {
            return new WelfareBean(source);
        }

        @Override
        public WelfareBean[] newArray(int size) {
            return new WelfareBean[size];
        }
    };
}
