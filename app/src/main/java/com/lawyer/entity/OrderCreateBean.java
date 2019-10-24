package com.lawyer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lawyer.controller.payment.PaymentFm;
import com.lawyer.enums.PayTypeEnum;
import com.lawyer.util.UserCache;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 @author wang
 on 2019/2/26 */

public class OrderCreateBean implements Parcelable {
    private int type;
    private BigDecimal money;
    private int projectId;
    private int caseId;
    private PayTypeEnum payType;
    private String orderCreatePath;
    private String tradeType;
    private String orderNo = "";

    //  银行卡类型(卡类型（00 –银行贷记账户;01 –银行借记账户;）)
    private String BkAcctTp;
    // 银行卡卡号
    private String bkAcctNo;
    // 证件类型(01：身份证)
    private String iDTp;
    //证件号
    private String iDNo;
    //持卡人姓名
    private String cstmrNm;
    //银行预留手机号
    private String mobNo;

    public String getTypeText() {
        try {
            return PaymentFm.type_pay_result_category.get(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "充值/捐款/案件费用支付";
//        return type == PaymentFm.type_welfare ? "1元开通会员服务" : "充值/捐款/案件费用支付";
    }

    public String getBkAcctTp() {
        return BkAcctTp;
    }

    public OrderCreateBean setBkAcctTp(String bkAcctTp) {
        BkAcctTp = bkAcctTp;
        return this;
    }

    public String getBkAcctNo() {
        return bkAcctNo;
    }

    public OrderCreateBean setBkAcctNo(String bkAcctNo) {
        this.bkAcctNo = bkAcctNo;
        return this;
    }

    public String getIDTp() {
        return iDTp;
    }

    public OrderCreateBean setIDTp(String iDTp) {
        this.iDTp = iDTp;
        return this;
    }

    public String getIDNo() {
        return iDNo;
    }

    public OrderCreateBean setIDNo(String iDNo) {
        this.iDNo = iDNo;
        return this;
    }

    public String getCstmrNm() {
        return cstmrNm;
    }

    public OrderCreateBean setCstmrNm(String cstmrNm) {
        this.cstmrNm = cstmrNm;
        return this;
    }

    public String getMobNo() {
        return mobNo;
    }

    public OrderCreateBean setMobNo(String mobNo) {
        this.mobNo = mobNo;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderCreateBean setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public OrderCreateBean setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public int getType() {
        return type;
    }

    public OrderCreateBean setType(int type) {
        this.type = type;
        return this;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public OrderCreateBean setMoney(BigDecimal money) {
        this.money = money;
        return this;
    }

    public int getProjectId() {
        return projectId;
    }

    public OrderCreateBean setProjectId(int projectId) {
        this.projectId = projectId;
        return this;
    }

    public PayTypeEnum getPayType() {
        return payType;
    }

    public OrderCreateBean setPayType(PayTypeEnum payType) {
        this.payType = payType;
        return this;
    }

    public int getCaseId() {
        return caseId;
    }

    public OrderCreateBean setCaseId(int caseId) {
        this.caseId = caseId;
        return this;
    }

    public String getOrderCreatePath() {
        return orderCreatePath;
    }

    public OrderCreateBean setOrderCreatePath(String orderCreatePath) {
        this.orderCreatePath = orderCreatePath;
        return this;
    }

    public OrderCreateBean() {
    }


    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (projectId != 0) {
            map.put("projectId", String.valueOf(projectId));
        }
        if (caseId != 0) {
            map.put("caseId", String.valueOf(caseId));
        }
        if (money != null) {
            map.put("money", money.toString());
        }
        if (payType != null) {
            map.put("payType", payType.name());
        }
        if (tradeType != null) {
            map.put("tradeType", tradeType);
        }
        if (bkAcctNo != null) {
            map.put("bkAcctNo", bkAcctNo);
            map.put("BkAcctTp", "01");
            map.put("iDTp", "01");
        }
        if (bkAcctNo != null) {
            map.put("bkAcctNo", bkAcctNo);
        }
        if (iDNo != null) {
            map.put("iDNo", iDNo);
        }
        if (cstmrNm != null) {
            map.put("cstmrNm", cstmrNm);
        }
        if (mobNo != null) {
            map.put("mobNo", mobNo);
        }
        map.put("accessToken", UserCache.getAccessToken());
        return map;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeSerializable(this.money);
        dest.writeInt(this.projectId);
        dest.writeInt(this.caseId);
        dest.writeInt(this.payType == null ? -1 : this.payType.ordinal());
        dest.writeString(this.orderCreatePath);
        dest.writeString(this.tradeType);
        dest.writeString(this.orderNo);
    }

    protected OrderCreateBean(Parcel in) {
        this.type = in.readInt();
        this.money = (BigDecimal) in.readSerializable();
        this.projectId = in.readInt();
        this.caseId = in.readInt();
        int tmpPayType = in.readInt();
        this.payType = tmpPayType == -1 ? null : PayTypeEnum.values()[tmpPayType];
        this.orderCreatePath = in.readString();
        this.tradeType = in.readString();
        this.orderNo = in.readString();
    }

    public static final Creator<OrderCreateBean> CREATOR = new Creator<OrderCreateBean>() {
        @Override
        public OrderCreateBean createFromParcel(Parcel source) {
            return new OrderCreateBean(source);
        }

        @Override
        public OrderCreateBean[] newArray(int size) {
            return new OrderCreateBean[size];
        }
    };
}
