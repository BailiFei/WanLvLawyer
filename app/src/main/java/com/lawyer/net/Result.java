package com.lawyer.net;

import ink.itwo.net.result.BaseResult;

/**
 @author wang
 on 2019/2/21 */

public class Result<T> extends BaseResult<T> {
    private int retCode, pageNum, pageSize, totalCount, totalPage;
    private String errorMsg;

    public int getRetCode() {
        return retCode;
    }

    public Result<T> setRetCode(int retCode) {
        this.retCode = retCode;
        return this;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public Result<T> setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public Result<T> setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Result<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Result<T> setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Result<T> setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    @Override
    public boolean isSuccess() {
        return retCode == 0;
    }

    @Override
    public int errorCode() {
        return retCode;
    }

    @Override
    public String errorMSG() {
        return errorMsg;
    }
}
