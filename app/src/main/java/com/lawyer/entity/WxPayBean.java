package com.lawyer.entity;

/**
 Created by wangtaian on 2019/3/30. */
public class WxPayBean {

    /**
     appid : wxabdf894faaca27c1
     noncestr : u3dkew6hxzp4esm542ib
     orderSn : 19033017050004603360
     packageStr : Sign=WXPay
     partnerid : 1530422201
     prepayid : wx30170511354962131f6be6ab2386065618
     sign : AFB0E823D206EC469D06934FE925F2CD
     timestamp : 1553936711
     */

    private String appid;
    private String noncestr;
    private String orderSn;
    private String packageStr;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
