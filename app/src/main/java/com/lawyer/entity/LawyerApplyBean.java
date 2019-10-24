package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.lawyer.enums.LawyerApplyStateEnum;
import com.lawyer.util.UserCache;

import java.util.HashMap;
import java.util.Map;

/**
 Created by wangtaian on 2019-05-30. */
public class LawyerApplyBean implements Parcelable {
    private Integer id;

    private Integer userId;

    private String mobileNo;

    private String name;

    private String gender;

    private String idCardNo;

    private String idCardFront;

    private String idCardBack;

    private String certificatePic;

    private String avatarUrl;

    private LawyerApplyStateEnum state;

    private String source;

    private String refusedReason;

    private String licenseNo;

    private Integer province;

    private Integer city;

    private String lawfirmName;

    private String address;

    private Integer skillFirst;

    private Integer skillSecond;

    private Integer skillThird;

    private String email;

    private String wechatId;

    private String qqId;

    private long createTime;

    private long updateTime;

    private String picUrl;

    private String skillFirstName;

    private String skillSecondName;

    private String skillThirdName;

    private String provinceName;

    private String cityName;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mobileNo", UserCache.get().getMobileNo());
        map.put("accessToken", UserCache.getAccessToken());
        map.put("name", name);
        if (!TextUtils.isEmpty(idCardNo)) map.put("idCardNo", idCardNo);
        if (!TextUtils.isEmpty(gender)) map.put("gender", gender);
        if (!TextUtils.isEmpty(idCardFront)) map.put("idCardFront", idCardFront);
        if (!TextUtils.isEmpty(idCardBack)) map.put("idCardBack", idCardBack);
        if (!TextUtils.isEmpty(certificatePic)) map.put("certificatePic", certificatePic);
        if (!TextUtils.isEmpty(licenseNo)) map.put("licenseNo", licenseNo);
        if (province != null) map.put("province", province);
        if (province != null) map.put("city", city);
        if (!TextUtils.isEmpty(lawfirmName)) map.put("lawfirmName", lawfirmName);
        if (!TextUtils.isEmpty(address)) map.put("address", address);
        if (skillFirst != null) map.put("skillFirst", skillFirst);
        if (skillSecond != null) map.put("skillSecond", skillSecond);
        if (skillThird != null) map.put("skillThird", skillThird);
        if (!TextUtils.isEmpty(email)) map.put("email", email);
        if (!TextUtils.isEmpty(wechatId)) map.put("wechatId", wechatId);
        if (!TextUtils.isEmpty(qqId)) map.put("qqId", qqId);
        return map;
    }

    public Integer getId() {
        return id;
    }

    public LawyerApplyBean setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public LawyerApplyBean setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public LawyerApplyBean setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public String getName() {
        return name;
    }

    public LawyerApplyBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public LawyerApplyBean setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public LawyerApplyBean setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
        return this;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public LawyerApplyBean setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
        return this;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public LawyerApplyBean setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
        return this;
    }

    public String getCertificatePic() {
        return certificatePic;
    }

    public LawyerApplyBean setCertificatePic(String certificatePic) {
        this.certificatePic = certificatePic;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LawyerApplyBean setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public LawyerApplyStateEnum getState() {
        return state;
    }

    public LawyerApplyBean setState(LawyerApplyStateEnum state) {
        this.state = state;
        return this;
    }

    public String getSource() {
        return source;
    }

    public LawyerApplyBean setSource(String source) {
        this.source = source;
        return this;
    }

    public String getRefusedReason() {
        return refusedReason;
    }

    public LawyerApplyBean setRefusedReason(String refusedReason) {
        this.refusedReason = refusedReason;
        return this;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public LawyerApplyBean setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
        return this;
    }

    public Integer getProvince() {
        return province;
    }

    public LawyerApplyBean setProvince(Integer province) {
        this.province = province;
        return this;
    }

    public Integer getCity() {
        return city;
    }

    public LawyerApplyBean setCity(Integer city) {
        this.city = city;
        return this;
    }

    public String getLawfirmName() {
        return lawfirmName;
    }

    public LawyerApplyBean setLawfirmName(String lawfirmName) {
        this.lawfirmName = lawfirmName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LawyerApplyBean setAddress(String address) {
        this.address = address;
        return this;
    }

    public Integer getSkillFirst() {
        return skillFirst;
    }

    public LawyerApplyBean setSkillFirst(Integer skillFirst) {
        this.skillFirst = skillFirst;
        return this;
    }

    public Integer getSkillSecond() {
        return skillSecond;
    }

    public LawyerApplyBean setSkillSecond(Integer skillSecond) {
        this.skillSecond = skillSecond;
        return this;
    }

    public Integer getSkillThird() {
        return skillThird;
    }

    public LawyerApplyBean setSkillThird(Integer skillThird) {
        this.skillThird = skillThird;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LawyerApplyBean setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWechatId() {
        return wechatId;
    }

    public LawyerApplyBean setWechatId(String wechatId) {
        this.wechatId = wechatId;
        return this;
    }

    public String getQqId() {
        return qqId;
    }

    public LawyerApplyBean setQqId(String qqId) {
        this.qqId = qqId;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public LawyerApplyBean setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public LawyerApplyBean setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public LawyerApplyBean setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }

    public String getSkillFirstName() {
        return skillFirstName;
    }

    public LawyerApplyBean setSkillFirstName(String skillFirstName) {
        this.skillFirstName = skillFirstName;
        return this;
    }

    public String getSkillSecondName() {
        return skillSecondName;
    }

    public LawyerApplyBean setSkillSecondName(String skillSecondName) {
        this.skillSecondName = skillSecondName;
        return this;
    }

    public String getSkillThirdName() {
        return skillThirdName;
    }

    public LawyerApplyBean setSkillThirdName(String skillThirdName) {
        this.skillThirdName = skillThirdName;
        return this;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public LawyerApplyBean setProvinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public LawyerApplyBean setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public LawyerApplyBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeString(this.mobileNo);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.idCardNo);
        dest.writeString(this.idCardFront);
        dest.writeString(this.idCardBack);
        dest.writeString(this.certificatePic);
        dest.writeString(this.avatarUrl);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeString(this.source);
        dest.writeString(this.refusedReason);
        dest.writeString(this.licenseNo);
        dest.writeValue(this.province);
        dest.writeValue(this.city);
        dest.writeString(this.lawfirmName);
        dest.writeString(this.address);
        dest.writeValue(this.skillFirst);
        dest.writeValue(this.skillSecond);
        dest.writeValue(this.skillThird);
        dest.writeString(this.email);
        dest.writeString(this.wechatId);
        dest.writeString(this.qqId);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeString(this.picUrl);
        dest.writeString(this.skillFirstName);
        dest.writeString(this.skillSecondName);
        dest.writeString(this.skillThirdName);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
    }

    protected LawyerApplyBean(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mobileNo = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.idCardNo = in.readString();
        this.idCardFront = in.readString();
        this.idCardBack = in.readString();
        this.certificatePic = in.readString();
        this.avatarUrl = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : LawyerApplyStateEnum.values()[tmpState];
        this.source = in.readString();
        this.refusedReason = in.readString();
        this.licenseNo = in.readString();
        this.province = (Integer) in.readValue(Integer.class.getClassLoader());
        this.city = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lawfirmName = in.readString();
        this.address = in.readString();
        this.skillFirst = (Integer) in.readValue(Integer.class.getClassLoader());
        this.skillSecond = (Integer) in.readValue(Integer.class.getClassLoader());
        this.skillThird = (Integer) in.readValue(Integer.class.getClassLoader());
        this.email = in.readString();
        this.wechatId = in.readString();
        this.qqId = in.readString();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.picUrl = in.readString();
        this.skillFirstName = in.readString();
        this.skillSecondName = in.readString();
        this.skillThirdName = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
    }

    public static final Creator<LawyerApplyBean> CREATOR = new Creator<LawyerApplyBean>() {
        @Override
        public LawyerApplyBean createFromParcel(Parcel source) {
            return new LawyerApplyBean(source);
        }

        @Override
        public LawyerApplyBean[] newArray(int size) {
            return new LawyerApplyBean[size];
        }
    };
}
