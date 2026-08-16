package com.railway.dao;

import java.math.BigDecimal;
import java.util.List;
import com.railway.model.TrainFare;

public interface FareDAO {
    boolean addFare(TrainFare fare);
    boolean updateFare(TrainFare fare);
    TrainFare getFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId);
    List<TrainFare> getFaresByTrain(int trainId);
    BigDecimal calculateFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId);
}
