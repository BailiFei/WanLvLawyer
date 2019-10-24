package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/2/22 */

public class LawyerIndexBean {
    private List<Banner> banner;
    private List<UserCaseBean> list;
    private User user;

    public List<Banner> getBanner() {
        return banner;
    }

    public LawyerIndexBean setBanner(List<Banner> banner) {
        this.banner = banner;
        return this;
    }

    public List<UserCaseBean> getList() {
        return list;
    }

    public LawyerIndexBean setList(List<UserCaseBean> list) {
        this.list = list;
        return this;
    }

    public User getUser() {
        return user;
    }

    public LawyerIndexBean setUser(User user) {
        this.user = user;
        return this;
    }
}
