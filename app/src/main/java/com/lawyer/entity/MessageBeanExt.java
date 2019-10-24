package com.lawyer.entity;

import java.util.List;

/**
 @author wang
 on 2019/3/18 */

public class MessageBeanExt {
    private List<MessageBean> list;

    public List<MessageBean> getList() {
        return list;
    }

    public MessageBeanExt setList(List<MessageBean> list) {
        this.list = list;
        return this;
    }
}
