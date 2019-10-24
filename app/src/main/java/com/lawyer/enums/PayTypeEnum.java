package com.lawyer.enums;

/**
 @author wang
 on 2019/2/26 */

public enum PayTypeEnum {
    alipay("支付宝支付"),

    wechatpay("微信支付"),

    wechatXcxPay("微信小程序支付"),

    wechatpayApp("移动端app微信支付"),

    applepay("苹果内购"),
    balance("余额支付"),
    offline("线下支付"),
    unionpay("银行卡支付"),

    lianlianpay("连连支付");

    private String description;

    PayTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
