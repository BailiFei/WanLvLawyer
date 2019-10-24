package com.m7.imkfsdk.chat.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by superXu on 2019/1/23
 */
public class Option implements Serializable {


    public String _id;
    public String name;
    public String key;
    public List<Option> options;
    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
