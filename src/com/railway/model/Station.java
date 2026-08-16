package com.railway.model;

import java.sql.Timestamp;

public class Station {
    private int stationId;
    private String stationCode;
    private String stationName;
    private String city;
    private String state;
    private String zone;
    private Timestamp createdAt;

    public Station() {
    }

    public Station(int stationId, String stationCode, String stationName, String city, String state, String zone, Timestamp createdAt) {
        this.stationId = stationId;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.city = city;
        this.state = state;
        this.zone = zone;
        this.createdAt = createdAt;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId=" + stationId +
                ", stationCode='" + stationCode + '\'' +
                ", stationName='" + stationName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
