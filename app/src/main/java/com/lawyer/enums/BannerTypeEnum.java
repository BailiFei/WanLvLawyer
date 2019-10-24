package com.lawyer.enums;

/**
 @author wang
 on 2019/2/21 */

public enum  BannerTypeEnum {
 pc("pc端"),

 app("app端"),

 example("案例"),

 video("短剧"),

 about("关于我们"),

 expert("专家");

 private String description;

 BannerTypeEnum(String description){
  this.description = description;
 }

 public String getDescription() {
  return description;
 }
}
