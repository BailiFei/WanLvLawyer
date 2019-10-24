package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lawyer.enums.AccountTypeEnum;
import com.lawyer.enums.WithdrawStateEnum;

import java.math.BigDecimal;

/**
 Created by wangtaian on 2019/4/25. */
public class CapitalWithdrawBean implements Parcelable {
    private int         userId;

    private AccountTypeEnum accountType;

    private BigDecimal amount;
    private String applyAmount;

    private String          withdrawOrderId;

    private BigDecimal      serviceFee;

    private String          type;

    private String          name;

    private String          payTransactionId;

    private String          bankName;

    private String          bankAccount;

    private String          wechatAccount;

    private String          alipayAccount;

    private String          remark;

    private WithdrawStateEnum state;

    private long verifyTime;

    private String          rejectRemark;

    private String          startDay;

    private String          endDay;

    private BigDecimal      totalAmount;

    private  String         bankAddr;

    private String          province;

    private String          city;

    private String          area;

    private String          district;

    private String          mobileNo;

    public String getApplyAmount() {
        return applyAmount;
    }

    public CapitalWithdrawBean setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public CapitalWithdrawBean setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public CapitalWithdrawBean setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CapitalWithdrawBean setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getWithdrawOrderId() {
        return withdrawOrderId;
    }

    public CapitalWithdrawBean setWithdrawOrderId(String withdrawOrderId) {
        this.withdrawOrderId = withdrawOrderId;
        return this;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public CapitalWithdrawBean setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
        return this;
    }

    public String getType() {
        return type;
    }

    public CapitalWithdrawBean setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public CapitalWithdrawBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getPayTransactionId() {
        return payTransactionId;
    }

    public CapitalWithdrawBean setPayTransactionId(String payTransactionId) {
        this.payTransactionId = payTransactionId;
        return this;
    }

    public String getBankName() {
        return bankName;
    }

    public CapitalWithdrawBean setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public CapitalWithdrawBean setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public CapitalWithdrawBean setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
        return this;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public CapitalWithdrawBean setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public CapitalWithdrawBean setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public WithdrawStateEnum getState() {
        return state;
    }

    public CapitalWithdrawBean setState(WithdrawStateEnum state) {
        this.state = state;
        return this;
    }

    public long getVerifyTime() {
        return verifyTime;
    }

    public CapitalWithdrawBean setVerifyTime(long verifyTime) {
        this.verifyTime = verifyTime;
        return this;
    }

    public String getRejectRemark() {
        return rejectRemark;
    }

    public CapitalWithdrawBean setRejectRemark(String rejectRemark) {
        this.rejectRemark = rejectRemark;
        return this;
    }

    public String getStartDay() {
        return startDay;
    }

    public CapitalWithdrawBean setStartDay(String startDay) {
        this.startDay = startDay;
        return this;
    }

    public String getEndDay() {
        return endDay;
    }

    public CapitalWithdrawBean setEndDay(String endDay) {
        this.endDay = endDay;
        return this;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public CapitalWithdrawBean setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getBankAddr() {
        return bankAddr;
    }

    public CapitalWithdrawBean setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public CapitalWithdrawBean setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CapitalWithdrawBean setCity(String city) {
        this.city = city;
        return this;
    }

    public String getArea() {
        return area;
    }

    public CapitalWithdrawBean setArea(String area) {
        this.area = area;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public CapitalWithdrawBean setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public CapitalWithdrawBean setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public CapitalWithdrawBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.accountType == null ? -1 : this.accountType.ordinal());
        dest.writeSerializable(this.amount);
        dest.writeString(this.applyAmount);
        dest.writeString(this.withdrawOrderId);
        dest.writeSerializable(this.serviceFee);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.payTransactionId);
        dest.writeString(this.bankName);
        dest.writeString(this.bankAccount);
        dest.writeString(this.wechatAccount);
        dest.writeString(this.alipayAccount);
        dest.writeString(this.remark);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeLong(this.verifyTime);
        dest.writeString(this.rejectRemark);
        dest.writeString(this.startDay);
        dest.writeString(this.endDay);
        dest.writeSerializable(this.totalAmount);
        dest.writeString(this.bankAddr);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.district);
        dest.writeString(this.mobileNo);
    }

    protected CapitalWithdrawBean(Parcel in) {
        this.userId = in.readInt();
        int tmpAccountType = in.readInt();
        this.accountType = tmpAccountType == -1 ? null : AccountTypeEnum.values()[tmpAccountType];
        this.amount = (BigDecimal) in.readSerializable();
        this.applyAmount = in.readString();
        this.withdrawOrderId = in.readString();
        this.serviceFee = (BigDecimal) in.readSerializable();
        this.type = in.readString();
        this.name = in.readString();
        this.payTransactionId = in.readString();
        this.bankName = in.readString();
        this.bankAccount = in.readString();
        this.wechatAccount = in.readString();
        this.alipayAccount = in.readString();
        this.remark = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : WithdrawStateEnum.values()[tmpState];
        this.verifyTime = in.readLong();
        this.rejectRemark = in.readString();
        this.startDay = in.readString();
        this.endDay = in.readString();
        this.totalAmount = (BigDecimal) in.readSerializable();
        this.bankAddr = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.district = in.readString();
        this.mobileNo = in.readString();
    }

    public static final Creator<CapitalWithdrawBean> CREATOR = new Creator<CapitalWithdrawBean>() {
        @Override
        public CapitalWithdrawBean createFromParcel(Parcel source) {
            return new CapitalWithdrawBean(source);
        }

        @Override
        public CapitalWithdrawBean[] newArray(int size) {
            return new CapitalWithdrawBean[size];
        }
    };
}
