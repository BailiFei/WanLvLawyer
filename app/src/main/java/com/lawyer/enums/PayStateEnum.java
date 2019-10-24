package com.lawyer.enums;

/**
 @author wang
 on 2019/2/28 */

public enum PayStateEnum {
    //    unpaid("未支付"),
//
//    paid("已支付"),
//    noorder("不存在订单"),
//    refund("已退款");
    unpaid("未支付"), // 待支付
    noorder("不存在订单"),

    cancelled("已取消"), // 已取消

    paid("已支付"), // 已支付

    done("已完成"), // 完成,已评价

    closed("已关闭"), // 已关闭

    deleted("已删除"), // 已删除

    refund("已退款"); // 已退款


    private String description;

    PayStateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
