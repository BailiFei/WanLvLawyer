package com.lawyer.enums;

/**
 @author wang
 on 2019/2/26 */

public enum  WelfareStateEnum {
 progressing("进行中"),

 finished("已完成");

 private String description;

 WelfareStateEnum(String description){
  this.description = description;
 }

 public String getDescription() {
  return description;
 }
}
