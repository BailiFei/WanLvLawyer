package com.lawyer.enums;

/**
 @author wang
 on 2019/3/16 */

public enum  CaseApplyStateEnum {
    //招标中：applying，中标success 未中标fail
    applying("申请中"),

    noapply("未申请"),

    success("成功"),

    fail("失败");

    private String description;

    CaseApplyStateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    public String getStateStr() {
        if (this==applying) return "已投";
        if (fail==this) return "失败";
        return "投标";
    }
}
