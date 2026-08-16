package com.railway.dao;

import java.sql.Date;
import java.util.List;
import com.railway.model.TrainSchedule;

public interface TrainScheduleDAO {
    boolean addOrUpdateSchedule(TrainSchedule schedule);
    TrainSchedule getSchedule(int trainId, Date journeyDate);
    List<TrainSchedule> getSchedulesByTrain(int trainId);
    List<TrainSchedule> getSchedulesByDate(Date journeyDate);
}
