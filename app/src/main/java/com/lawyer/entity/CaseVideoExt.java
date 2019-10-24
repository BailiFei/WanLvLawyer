package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/25 */

public class CaseVideoExt {
    private List<VideoBean> list;
    private List<CaseTypeBean> caseTypeList;

    public List<VideoBean> getList() {
        return list;
    }

    public CaseVideoExt setList(List<VideoBean> list) {
        this.list = list;
        return this;
    }

    public List<CaseTypeBean> getCaseTypeList() {
        return caseTypeList;
    }

    public CaseVideoExt setCaseTypeList(List<CaseTypeBean> caseTypeList) {
        this.caseTypeList = caseTypeList;
        return this;
    }
}
