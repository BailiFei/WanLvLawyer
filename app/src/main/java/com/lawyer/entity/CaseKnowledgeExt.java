package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/26 */

public class CaseKnowledgeExt {
    private List<CaseKnowledgeBean> list;
    private List<CaseTypeBean> caseTypeList;

    public List<CaseKnowledgeBean> getList() {
        return list;
    }

    public CaseKnowledgeExt setList(List<CaseKnowledgeBean> list) {
        this.list = list;
        return this;
    }

    public List<CaseTypeBean> getCaseTypeList() {
        return caseTypeList;
    }

    public CaseKnowledgeExt setCaseTypeList(List<CaseTypeBean> caseTypeList) {
        this.caseTypeList = caseTypeList;
        return this;
    }
}
