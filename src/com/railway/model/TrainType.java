package com.railway.model;

public class TrainType {
    private int trainTypeId;
    private String typeName;
    private String description;

    public TrainType() {
    }

    public TrainType(int trainTypeId, String typeName, String description) {
        this.trainTypeId = trainTypeId;
        this.typeName = typeName;
        this.description = description;
    }

    public int getTrainTypeId() {
        return trainTypeId;
    }

    public void setTrainTypeId(int trainTypeId) {
        this.trainTypeId = trainTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TrainType{" +
                "trainTypeId=" + trainTypeId +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}