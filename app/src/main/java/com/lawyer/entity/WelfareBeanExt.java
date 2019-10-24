package com.lawyer.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 @author wang
 on 2019/2/26 */

public class WelfareBeanExt {
    private List<WelfareBean> list;

    private int pageNum;
    private int pageSize;
    private int totalCount;
    private BigDecimal totalMoney;
    private int totalHelpPeole;
    private int totalHelpCount;

    public List<WelfareBean> getList() {
        return list;
    }

    public WelfareBeanExt setList(List<WelfareBean> list) {
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

    public int getTotalHelpCount() {
        return totalHelpCount;
    }

    public void setTotalHelpCount(int totalHelpCount) {
        this.totalHelpCount = totalHelpCount;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public WelfareBeanExt setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
        return this;
    }

    public int getTotalHelpPeole() {
        return totalHelpPeole;
    }

    public void setTotalHelpPeole(int totalHelpPeole) {
        this.totalHelpPeole = totalHelpPeole;
    }


}
