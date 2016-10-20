package com.flycode.transporttrackeryerevan.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 10/17/16 __ Schumakher .
 */

public class ItemBus {
    @SerializedName("_id")
    private String id;
    private int busNumber;
    private String azimuth;
    private int speed;
    private String imei;
    private double[] geo;
    private String updated;

    public ItemBus(String id, int busNumber, String azimuth, int speed, String imei, double[] geo, String updated) {
        this.id = id;
        this.busNumber = busNumber;
        this.azimuth = azimuth;
        this.speed = speed;
        this.imei = imei;
        this.geo = geo;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double[] getGeo() {
        return geo;
    }

    public void setGeo(double[] geo) {
        this.geo = geo;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
