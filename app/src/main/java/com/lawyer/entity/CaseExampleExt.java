package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/25 */

public class CaseExampleExt {
    private List<CaseExampleBean> list;
    private List<CaseTypeBean> caseTypeList;

    public List<CaseExampleBean> getList() {
        return list;
    }

    public CaseExampleExt setList(List<CaseExampleBean> list) {
        this.list = list;
        return this;
    }

    public List<CaseTypeBean> getCaseTypeList() {
        return caseTypeList;
    }

    public CaseExampleExt setCaseTypeList(List<CaseTypeBean> caseTypeList) {
        this.caseTypeList = caseTypeList;
        return this;
    }
}
