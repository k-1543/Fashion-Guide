package com.fashionapp.fashionapp;

public class Shops
{
    String name, id, lat, lng;

    public Shops(){}
    public String getName() {
        return name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {

        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Shops(String name, String id, String lat, String lng) {

        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;

    }

    public void setName(String name) {

        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Shops(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
