package com.railway.dao;

import java.util.List;
import com.railway.model.TrainCoach;

public interface CoachDAO {
    boolean addCoach(TrainCoach coach);
    boolean updateCoach(TrainCoach coach);
    boolean setCoachActive(int coachId, boolean active);
    TrainCoach getCoachById(int coachId);
    List<TrainCoach> getCoachesByTrainId(int trainId);
    List<TrainCoach> getCoachesByTrainAndType(int trainId, int coachTypeId);
}
