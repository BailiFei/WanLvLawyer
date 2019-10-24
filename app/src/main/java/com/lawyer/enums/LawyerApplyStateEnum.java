package com.lawyer.enums;

/**
 Created by wangtaian on 2019-05-30. */
public enum LawyerApplyStateEnum {
    //applied(已申请),refused(已拒绝),passed(已通过),notapply
    normal("正常"),

    applied("已申请"),

    refused("已拒绝"),

    passed("已通过");

    private String description;

    LawyerApplyStateEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
