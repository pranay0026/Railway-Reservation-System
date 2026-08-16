package com.railway.model;

public class CoachType {
    private int coachTypeId;
    private String coachName;
    private String description;

    public CoachType() {
    }

    public CoachType(int coachTypeId, String coachName, String description) {
        this.coachTypeId = coachTypeId;
        this.coachName = coachName;
        this.description = description;
    }

    public int getCoachTypeId() {
        return coachTypeId;
    }

    public void setCoachTypeId(int coachTypeId) {
        this.coachTypeId = coachTypeId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CoachType{" +
                "coachTypeId=" + coachTypeId +
                ", coachName='" + coachName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
