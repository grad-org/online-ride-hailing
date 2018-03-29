package com.gd.orh.domain;

public class Location {
    private String lng; // 经度
    private String lat; // 纬度

    public Location() {
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
