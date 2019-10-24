package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lawyer.enums.AccountTypeEnum;
import com.lawyer.enums.CaseApplyStateEnum;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.enums.PayStateEnum;
import com.lawyer.util.UserCache;

import java.math.BigDecimal;

/**
 @author wang
 on 2019/2/22 */

public class UserCaseBean implements Parcelable {

    private int id;

    private int userId;

    private int lawyerId;

    private int caseType;

    private String title;

    private CaseStateEnum state;

    private int province;

    private int city;

    private int district;

    private String address;

    private int viewcount;

    private int tendercount;

    private int operateUserId;

    private long createTime;

    private long updateTime;

    private String content;

    private String provinceName;

    private String cityName;

    private String caseTypeName;

    private String userMobileNo;

    private String lawyerMobileNo;

    private int caseId;

    private String userName;

    private String lawyerName;
    private String timeText;
    private String avatarUrl;
    private String lawyerAvatarUrl;
    private BigDecimal needPayMoney;
    private PayStateEnum payState;
    private CaseApplyStateEnum caseApplyState;


    private boolean biddingEnable;

    public boolean getBiddingEnable() {
        return state == CaseStateEnum.tendering && caseApplyState == CaseApplyStateEnum.noapply;
    }

    public UserCaseBean setBiddingEnable(boolean biddingEnable) {
        this.biddingEnable = biddingEnable;
        return this;
    }

    public PayStateEnum getPayState() {
        return payState;
    }

    public UserCaseBean setPayState(PayStateEnum payState) {
        this.payState = payState;
        return this;
    }

    public CaseApplyStateEnum getCaseApplyState() {
        return caseApplyState;
    }

    public UserCaseBean setCaseApplyState(CaseApplyStateEnum caseApplyState) {
        this.caseApplyState = caseApplyState;
        return this;
    }

    public String getCityStr() {
        return (provinceName == null ? "" : provinceName) + (cityName == null ? "" : cityName);
    }

    public boolean getUnpaid() {
        return needPayMoney != null && needPayMoney.compareTo(BigDecimal.ZERO) > 0
                && state == CaseStateEnum.solving && UserCache.get() != null && UserCache.get().getAccountType() == AccountTypeEnum.user;
    }

    public String getLawyerAvatarUrl() {
        return lawyerAvatarUrl;
    }

    public UserCaseBean setLawyerAvatarUrl(String lawyerAvatarUrl) {
        this.lawyerAvatarUrl = lawyerAvatarUrl;
        return this;
    }

    public int getId() {
        return id;
    }

    public UserCaseBean setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public UserCaseBean setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getLawyerId() {
        return lawyerId;
    }

    public UserCaseBean setLawyerId(int lawyerId) {
        this.lawyerId = lawyerId;
        return this;
    }

    public int getCaseType() {
        return caseType;
    }

    public UserCaseBean setCaseType(int caseType) {
        this.caseType = caseType;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UserCaseBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public CaseStateEnum getState() {
        return state;
    }

    public UserCaseBean setState(CaseStateEnum state) {
        this.state = state;
        return this;
    }

    public int getProvince() {
        return province;
    }

    public UserCaseBean setProvince(int province) {
        this.province = province;
        return this;
    }

    public int getCity() {
        return city;
    }

    public UserCaseBean setCity(int city) {
        this.city = city;
        return this;
    }

    public int getDistrict() {
        return district;
    }

    public UserCaseBean setDistrict(int district) {
        this.district = district;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserCaseBean setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getViewcount() {
        return viewcount;
    }

    public UserCaseBean setViewcount(int viewcount) {
        this.viewcount = viewcount;
        return this;
    }

    public int getTendercount() {
        return tendercount;
    }

    public UserCaseBean setTendercount(int tendercount) {
        this.tendercount = tendercount;
        return this;
    }

    public int getOperateUserId() {
        return operateUserId;
    }

    public UserCaseBean setOperateUserId(int operateUserId) {
        this.operateUserId = operateUserId;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public UserCaseBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public UserCaseBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public UserCaseBean setContent(String content) {
        this.content = content;
        return this;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public UserCaseBean setProvinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public UserCaseBean setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public UserCaseBean setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
        return this;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public UserCaseBean setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
        return this;
    }

    public String getLawyerMobileNo() {
        return lawyerMobileNo;
    }

    public UserCaseBean setLawyerMobileNo(String lawyerMobileNo) {
        this.lawyerMobileNo = lawyerMobileNo;
        return this;
    }

    public int getCaseId() {
        return caseId;
    }

    public UserCaseBean setCaseId(int caseId) {
        this.caseId = caseId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserCaseBean setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public UserCaseBean setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
        return this;
    }

    public String getTimeText() {
        return timeText;
    }

    public UserCaseBean setTimeText(String timeText) {
        this.timeText = timeText;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserCaseBean setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public BigDecimal getNeedPayMoney() {
        return needPayMoney;
    }

    public UserCaseBean setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
        return this;
    }

    public UserCaseBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeInt(this.lawyerId);
        dest.writeInt(this.caseType);
        dest.writeString(this.title);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeInt(this.district);
        dest.writeString(this.address);
        dest.writeInt(this.viewcount);
        dest.writeInt(this.tendercount);
        dest.writeInt(this.operateUserId);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeString(this.content);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeString(this.caseTypeName);
        dest.writeString(this.userMobileNo);
        dest.writeString(this.lawyerMobileNo);
        dest.writeInt(this.caseId);
        dest.writeString(this.userName);
        dest.writeString(this.lawyerName);
        dest.writeString(this.timeText);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.lawyerAvatarUrl);
        dest.writeSerializable(this.needPayMoney);
        dest.writeInt(this.payState == null ? -1 : this.payState.ordinal());
        dest.writeInt(this.caseApplyState == null ? -1 : this.caseApplyState.ordinal());
        dest.writeByte(this.biddingEnable ? (byte) 1 : (byte) 0);
    }

    protected UserCaseBean(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.lawyerId = in.readInt();
        this.caseType = in.readInt();
        this.title = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : CaseStateEnum.values()[tmpState];
        this.province = in.readInt();
        this.city = in.readInt();
        this.district = in.readInt();
        this.address = in.readString();
        this.viewcount = in.readInt();
        this.tendercount = in.readInt();
        this.operateUserId = in.readInt();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.content = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.caseTypeName = in.readString();
        this.userMobileNo = in.readString();
        this.lawyerMobileNo = in.readString();
        this.caseId = in.readInt();
        this.userName = in.readString();
        this.lawyerName = in.readString();
        this.timeText = in.readString();
        this.avatarUrl = in.readString();
        this.lawyerAvatarUrl = in.readString();
        this.needPayMoney = (BigDecimal) in.readSerializable();
        int tmpPayState = in.readInt();
        this.payState = tmpPayState == -1 ? null : PayStateEnum.values()[tmpPayState];
        int tmpCaseApplyState = in.readInt();
        this.caseApplyState = tmpCaseApplyState == -1 ? null : CaseApplyStateEnum.values()[tmpCaseApplyState];
        this.biddingEnable = in.readByte() != 0;
    }

    public static final Creator<UserCaseBean> CREATOR = new Creator<UserCaseBean>() {
        @Override
        public UserCaseBean createFromParcel(Parcel source) {
            return new UserCaseBean(source);
        }

        @Override
        public UserCaseBean[] newArray(int size) {
            return new UserCaseBean[size];
        }
    };
}
