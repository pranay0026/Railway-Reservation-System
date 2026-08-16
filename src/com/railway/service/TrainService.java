package com.railway.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.railway.exception.RailwayException;
import com.railway.model.CoachSeat;
import com.railway.model.CoachType;
import com.railway.model.Station;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.TrainRoute;
import com.railway.model.TrainSchedule;

public interface TrainService {
    List<Train> searchTrains(String sourceCode, String destCode, LocalDate journeyDate) throws RailwayException;
    List<TrainCoach> getCoachesByTrain(int trainId);
    List<CoachType> getAllCoachTypes();
    List<CoachSeat> getAvailableSeats(int trainId, int coachTypeId, LocalDate journeyDate);
    int getAvailableSeatsCount(int trainId, int coachTypeId, LocalDate journeyDate);
    Map<String, Integer> getSeatAvailabilityByClass(int trainId, LocalDate journeyDate);
    BigDecimal getFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId);
    Train getTrainById(int trainId);
    Train getTrainByNumber(String trainNumber);
    Station getStationByCode(String code);
    Station getStationById(int stationId);
    List<Station> searchStations(String query);
    List<Station> getAllStations();
    List<TrainRoute> getTrainRoute(int trainId);
    TrainSchedule getSchedule(int trainId, LocalDate journeyDate);
}
