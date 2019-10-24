package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/26 */

public class DataExt<T> {

    /**
     pageNum : 0
     pageSize : 20
     totalCount : 3
     */

    private int pageNum;
    private int pageSize;
    private int totalCount;
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public DataExt<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
