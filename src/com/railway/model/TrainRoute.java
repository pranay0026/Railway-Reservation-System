package com.railway.model;

import java.sql.Time;

public class TrainRoute {
    private int routeId;
    private int trainId;
    private int stationId;
    private int stopNumber;
    private Time arrivalTime;
    private Time departureTime;
    private int distanceFromSource;
    private String platformNumber;

    // Helper display fields
    private String stationCode;
    private String stationName;
    private String trainNumber;
    private String trainName;

    public TrainRoute() {
    }

    public TrainRoute(int routeId, int trainId, int stationId, int stopNumber, Time arrivalTime, Time departureTime, int distanceFromSource, String platformNumber) {
        this.routeId = routeId;
        this.trainId = trainId;
        this.stationId = stationId;
        this.stopNumber = stopNumber;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.distanceFromSource = distanceFromSource;
        this.platformNumber = platformNumber;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public int getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber;
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

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Override
    public String toString() {
        return "TrainRoute{" +
                "routeId=" + routeId +
                ", trainId=" + trainId +
                ", stationId=" + stationId +
                ", stopNumber=" + stopNumber +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", distanceFromSource=" + distanceFromSource +
                ", platformNumber='" + platformNumber + '\'' +
                '}';
    }
}