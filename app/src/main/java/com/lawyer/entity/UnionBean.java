package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/** 畅捷支付
 Created by wangtaian on 2019-06-06. */
public class UnionBean implements Parcelable {

    /**
     * AcceptStatus : S
     * AppRetMsg : 交易受理成功
     * AppRetcode : QT000001
     * InputCharset : UTF-8
     * OrderTrxid : 101155981338203196279
     * PartnerId : 200003860099
     * RetCode : P0002
     * RetMsg : 受理成功
     * Sign : E6Ua5zfmMUN3lpWFjYWTEbWGP5oUehTtS4Dnr8adRF+w+/MAr28nIC/8haGU9Wb99Ig1m9ye7NvOqrtaPiYJP15+0Kzi5L8V++KfzPbZVnU/ACnKaBpYAV9YemdDbQfRhpniiG5WaiFi9tLi2P1D74WldJ2ElnA1Im1g8ftcjRE=
     * SignType : RSA
     * Status : P
     * TradeDate : 20190606
     * TradeTime : 172942
     * TrxId : 19060617290013431790
     */

    private String AcceptStatus;
    private String AppRetMsg;
    private String AppRetcode;
    private String InputCharset;
    private String OrderTrxid;
    private String PartnerId;
    private String RetCode;
    private String RetMsg;
    private String Sign;
    private String SignType;
    private String Status;
    private String TradeDate;
    private String TradeTime;
    private String TrxId;

    public String getAcceptStatus() {
        return AcceptStatus;
    }

    public void setAcceptStatus(String AcceptStatus) {
        this.AcceptStatus = AcceptStatus;
    }

    public String getAppRetMsg() {
        return AppRetMsg;
    }

    public void setAppRetMsg(String AppRetMsg) {
        this.AppRetMsg = AppRetMsg;
    }

    public String getAppRetcode() {
        return AppRetcode;
    }

    public void setAppRetcode(String AppRetcode) {
        this.AppRetcode = AppRetcode;
    }

    public String getInputCharset() {
        return InputCharset;
    }

    public void setInputCharset(String InputCharset) {
        this.InputCharset = InputCharset;
    }

    public String getOrderTrxid() {
        return OrderTrxid;
    }

    public void setOrderTrxid(String OrderTrxid) {
        this.OrderTrxid = OrderTrxid;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String PartnerId) {
        this.PartnerId = PartnerId;
    }

    public String getRetCode() {
        return RetCode;
    }

    public void setRetCode(String RetCode) {
        this.RetCode = RetCode;
    }

    public String getRetMsg() {
        return RetMsg;
    }

    public void setRetMsg(String RetMsg) {
        this.RetMsg = RetMsg;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }

    public String getSignType() {
        return SignType;
    }

    public void setSignType(String SignType) {
        this.SignType = SignType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getTradeDate() {
        return TradeDate;
    }

    public void setTradeDate(String TradeDate) {
        this.TradeDate = TradeDate;
    }

    public String getTradeTime() {
        return TradeTime;
    }

    public void setTradeTime(String TradeTime) {
        this.TradeTime = TradeTime;
    }

    public String getTrxId() {
        return TrxId;
    }

    public void setTrxId(String TrxId) {
        this.TrxId = TrxId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.AcceptStatus);
        dest.writeString(this.AppRetMsg);
        dest.writeString(this.AppRetcode);
        dest.writeString(this.InputCharset);
        dest.writeString(this.OrderTrxid);
        dest.writeString(this.PartnerId);
        dest.writeString(this.RetCode);
        dest.writeString(this.RetMsg);
        dest.writeString(this.Sign);
        dest.writeString(this.SignType);
        dest.writeString(this.Status);
        dest.writeString(this.TradeDate);
        dest.writeString(this.TradeTime);
        dest.writeString(this.TrxId);
    }

    public UnionBean() {
    }

    protected UnionBean(Parcel in) {
        this.AcceptStatus = in.readString();
        this.AppRetMsg = in.readString();
        this.AppRetcode = in.readString();
        this.InputCharset = in.readString();
        this.OrderTrxid = in.readString();
        this.PartnerId = in.readString();
        this.RetCode = in.readString();
        this.RetMsg = in.readString();
        this.Sign = in.readString();
        this.SignType = in.readString();
        this.Status = in.readString();
        this.TradeDate = in.readString();
        this.TradeTime = in.readString();
        this.TrxId = in.readString();
    }

    public static final Parcelable.Creator<UnionBean> CREATOR = new Parcelable.Creator<UnionBean>() {
        @Override
        public UnionBean createFromParcel(Parcel source) {
            return new UnionBean(source);
        }

        @Override
        public UnionBean[] newArray(int size) {
            return new UnionBean[size];
        }
    };
}
