package com.lawyer.entity;

import android.text.TextUtils;

import com.lawyer.enums.AccountTypeEnum;

import java.math.BigDecimal;

/**
 @author wang
 on 2019/2/21 */

public class User {

    /**
     accessToken : E57C506A88F54BCDB8379C7638C9E10D
     accountType : user
     avatarUrl : http://lawfirm.oss-cn-hangzhou.aliyuncs.com/avatar/ft_default.png
     avatarUrlStr : http://lawfirm.oss-cn-hangzhou.aliyuncs.com/avatar/ft_default.png
     createTime : 1550726436493
     id : 52
     mobileNo : 18757197519
     nickName : 用户7519
     password : d1c2dcfcbfd41ce1103a25475b64f7a5
     state : normal
     userId : 52
     */

    private String accessToken;
    private AccountTypeEnum accountType;
    private String avatarUrl;
    private String avatarUrlStr;
    private long createTime;
    private int id;
    private String mobileNo;
    private String nickName;
    private String password;
    private String state;
    private int userId;
    private String idCardNo;
    private String name;
    private BigDecimal money;
    //是否会员 1是0否
    private int isVip;
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public User setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public int getIsVip() {
        return isVip;
    }

    public User setIsVip(int isVip) {
        this.isVip = isVip;
        return this;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public User setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public User setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public User setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrlStr() {
        return avatarUrlStr;
    }

    public void setAvatarUrlStr(String avatarUrlStr) {
        this.avatarUrlStr = avatarUrlStr;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getMobileText() {
        if (TextUtils.isEmpty(mobileNo) || mobileNo.length() != 11) return mobileNo;
        return mobileNo.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
