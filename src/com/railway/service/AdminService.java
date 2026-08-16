package com.railway.service;

import java.util.List;
import com.railway.exception.AuthenticationException;
import com.railway.exception.RailwayException;
import com.railway.model.Admin;
import com.railway.model.AuditLog;
import com.railway.model.CoachSeat;
import com.railway.model.Station;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.TrainFare;
import com.railway.model.TrainRoute;
import com.railway.model.TrainSchedule;

public interface AdminService {
    Admin login(String username, String password) throws AuthenticationException;
    boolean addAdmin(Admin admin) throws RailwayException;
    List<Admin> getAllAdmins();

    // Train management
    boolean addTrain(Train train) throws RailwayException;
    boolean updateTrain(Train train) throws RailwayException;
    boolean setTrainActive(int trainId, boolean active) throws RailwayException;
    List<Train> getAllTrains();

    // Station management
    boolean addStation(Station station) throws RailwayException;
    boolean updateStation(Station station) throws RailwayException;
    List<Station> getAllStations();

    // Route management
    boolean addRouteStop(TrainRoute route) throws RailwayException;
    boolean updateRouteStop(TrainRoute route) throws RailwayException;
    boolean deleteRouteStop(int routeId) throws RailwayException;
    List<TrainRoute> getRoutesByTrain(int trainId);

    // Coach & Seat management
    boolean addCoach(TrainCoach coach) throws RailwayException;
    boolean updateCoach(TrainCoach coach) throws RailwayException;
    boolean addSeatsToCoach(int coachId, int totalSeats, String defaultBerthDistribution) throws RailwayException;
    List<TrainCoach> getCoachesByTrain(int trainId);
    List<CoachSeat> getSeatsByCoach(int coachId);

    // Schedule management
    boolean addOrUpdateSchedule(TrainSchedule schedule) throws RailwayException;
    List<TrainSchedule> getSchedulesByTrain(int trainId);

    // Fare management
    boolean addFare(TrainFare fare) throws RailwayException;
    boolean updateFare(TrainFare fare) throws RailwayException;
    List<TrainFare> getFaresByTrain(int trainId);

    // Audit logs
    List<AuditLog> getAuditLogs(int limit);
}
