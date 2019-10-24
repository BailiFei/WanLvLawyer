package com.lawyer.enums;

/**
 Created by wangtaian on 2019/4/25. */
public enum  WithdrawStateEnum {
    applying("申请中"),

    approved("同意"),

    rejected("拒绝");

    private String description;

    WithdrawStateEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
