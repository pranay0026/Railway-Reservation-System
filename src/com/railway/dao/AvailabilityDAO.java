package com.railway.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import com.railway.model.TrainAvailability;

public interface AvailabilityDAO {
    TrainAvailability getAvailability(int trainId, int coachId, Date journeyDate);
    List<TrainAvailability> getAvailabilityForTrain(int trainId, Date journeyDate);
    boolean initializeAvailabilityIfAbsent(Connection conn, int trainId, int coachId, Date journeyDate, int totalSeats);
    boolean updateAvailability(Connection conn, int trainId, int coachId, Date journeyDate, int seatsDelta, int racDelta, int waitlistDelta);
    int getTotalAvailableSeatsForClass(int trainId, int coachTypeId, Date journeyDate);
}
