package com.lawyer.entity;

/**
 Created by wangtaian on 2019/3/25. */
public class CityBean {
    private String ip;
    private String province;
    private String city;

    public String getIp() {
        return ip;
    }

    public CityBean setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public CityBean setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CityBean setCity(String city) {
        this.city = city;
        return this;
    }
}
