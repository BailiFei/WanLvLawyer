package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lawyer.enums.StateEnum;

/**
 Created by wangtaian on 2019/4/25. */
public class BankCardBean implements Parcelable {
    private int id;

    private int userId;

    private String cardNo;

    private String bankName;

    private StateEnum state;

    private long createTime;

    private long updateTime;

    private String cardName;

    private String cardType;

    private String sex;

    private String area;

    private String province;

    private String city;

    private String prefecture;

    private String birthday;

    private String name;

    private String idCard;

    private String mobile;
    private String amount;

    public String getAmount() {
        return amount;
    }

    public BankCardBean setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public int getId() {
        return id;
    }

    public BankCardBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public BankCardBean setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getCardNo() {
        return cardNo;
    }

    public BankCardBean setCardNo(String cardNo) {
        this.cardNo = cardNo;
        return this;
    }

    public String getBankName() {
        return bankName;
    }

    public BankCardBean setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public StateEnum getState() {
        return state;
    }

    public BankCardBean setState(StateEnum state) {
        this.state = state;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public BankCardBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public BankCardBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getCardName() {
        return cardName;
    }

    public BankCardBean setCardName(String cardName) {
        this.cardName = cardName;
        return this;
    }

    public String getCardType() {
        return cardType;
    }

    public BankCardBean setCardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public BankCardBean setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getArea() {
        return area;
    }

    public BankCardBean setArea(String area) {
        this.area = area;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public BankCardBean setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public BankCardBean setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public BankCardBean setPrefecture(String prefecture) {
        this.prefecture = prefecture;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public BankCardBean setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getName() {
        return name;
    }

    public BankCardBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public BankCardBean setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public BankCardBean setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public BankCardBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.cardNo);
        dest.writeString(this.bankName);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeString(this.cardName);
        dest.writeString(this.cardType);
        dest.writeString(this.sex);
        dest.writeString(this.area);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.prefecture);
        dest.writeString(this.birthday);
        dest.writeString(this.name);
        dest.writeString(this.idCard);
        dest.writeString(this.mobile);
        dest.writeString(this.amount);
    }

    protected BankCardBean(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.cardNo = in.readString();
        this.bankName = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : StateEnum.values()[tmpState];
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.cardName = in.readString();
        this.cardType = in.readString();
        this.sex = in.readString();
        this.area = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.prefecture = in.readString();
        this.birthday = in.readString();
        this.name = in.readString();
        this.idCard = in.readString();
        this.mobile = in.readString();
        this.amount = in.readString();
    }

    public static final Creator<BankCardBean> CREATOR = new Creator<BankCardBean>() {
        @Override
        public BankCardBean createFromParcel(Parcel source) {
            return new BankCardBean(source);
        }

        @Override
        public BankCardBean[] newArray(int size) {
            return new BankCardBean[size];
        }
    };
}
