package com.lawyer.enums;

/**
 @author wang
 on 2019/2/21 */

public enum AccountTypeEnum {
    user("用户"),

    lawyer("律师"),

    auditor("审核员");

    private String description;

    AccountTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
