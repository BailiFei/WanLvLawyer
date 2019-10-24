package com.lawyer.enums;

/**
 Created by wangtaian on 2019/4/25. */
public enum  StateEnum {
    inactive("未激活"),

    activated("已激活"),

    normal("正常"),

    expired("已失效"),

    deleted("已删除");

    private String description;

    StateEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
