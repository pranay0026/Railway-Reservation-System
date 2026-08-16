package com.railway.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.railway.dao.AdminDAO;
import com.railway.dao.CoachDAO;
import com.railway.dao.FareDAO;
import com.railway.dao.SeatDAO;
import com.railway.dao.StationDAO;
import com.railway.dao.TrainDAO;
import com.railway.dao.TrainRouteDAO;
import com.railway.dao.TrainScheduleDAO;
import com.railway.dao.impl.AdminDAOImpl;
import com.railway.dao.impl.CoachDAOImpl;
import com.railway.dao.impl.FareDAOImpl;
import com.railway.dao.impl.SeatDAOImpl;
import com.railway.dao.impl.StationDAOImpl;
import com.railway.dao.impl.TrainDAOImpl;
import com.railway.dao.impl.TrainRouteDAOImpl;
import com.railway.dao.impl.TrainScheduleDAOImpl;
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
import com.railway.service.AdminService;

public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;
    private final TrainDAO trainDAO;
    private final StationDAO stationDAO;
    private final TrainRouteDAO routeDAO;
    private final CoachDAO coachDAO;
    private final SeatDAO seatDAO;
    private final TrainScheduleDAO scheduleDAO;
    private final FareDAO fareDAO;

    public AdminServiceImpl() {
        this.adminDAO = new AdminDAOImpl();
        this.trainDAO = new TrainDAOImpl();
        this.stationDAO = new StationDAOImpl();
        this.routeDAO = new TrainRouteDAOImpl();
        this.coachDAO = new CoachDAOImpl();
        this.seatDAO = new SeatDAOImpl();
        this.scheduleDAO = new TrainScheduleDAOImpl();
        this.fareDAO = new FareDAOImpl();
    }

    @Override
    public Admin login(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("Username and password cannot be empty.");
        }
        Admin admin = adminDAO.loginAdmin(username.trim(), password);
        if (admin == null) {
            throw new AuthenticationException("Invalid admin username or password.");
        }
        return admin;
    }

    @Override
    public boolean addAdmin(Admin admin) throws RailwayException {
        if (admin == null || admin.getUsername() == null || admin.getPassword() == null) {
            throw new RailwayException("Admin username and password are required.");
        }
        Admin existing = adminDAO.getAdminByUsername(admin.getUsername().trim());
        if (existing != null) {
            throw new RailwayException("Admin with username '" + admin.getUsername() + "' already exists.");
        }
        return adminDAO.addAdmin(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    @Override
    public boolean addTrain(Train train) throws RailwayException {
        if (train == null || train.getTrainNumber() == null || train.getTrainName() == null) {
            throw new RailwayException("Train number and name are required.");
        }
        Train existing = trainDAO.getTrainByNumber(train.getTrainNumber().trim());
        if (existing != null) {
            throw new RailwayException("Train number '" + train.getTrainNumber() + "' already exists.");
        }
        return trainDAO.addTrain(train);
    }

    @Override
    public boolean updateTrain(Train train) throws RailwayException {
        if (train == null || train.getTrainId() <= 0) {
            throw new RailwayException("Invalid train record for update.");
        }
        return trainDAO.updateTrain(train);
    }

    @Override
    public boolean setTrainActive(int trainId, boolean active) throws RailwayException {
        if (trainId <= 0) {
            throw new RailwayException("Invalid train ID.");
        }
        return trainDAO.setTrainActive(trainId, active);
    }

    @Override
    public List<Train> getAllTrains() {
        return trainDAO.getAllTrains();
    }

    @Override
    public boolean addStation(Station station) throws RailwayException {
        if (station == null || station.getStationCode() == null || station.getStationName() == null) {
            throw new RailwayException("Station code and name are required.");
        }
        Station existing = stationDAO.getStationByCode(station.getStationCode().trim());
        if (existing != null) {
            throw new RailwayException("Station code '" + station.getStationCode() + "' already exists.");
        }
        return stationDAO.addStation(station);
    }

    @Override
    public boolean updateStation(Station station) throws RailwayException {
        if (station == null || station.getStationId() <= 0) {
            throw new RailwayException("Invalid station record for update.");
        }
        return stationDAO.updateStation(station);
    }

    @Override
    public List<Station> getAllStations() {
        return stationDAO.getAllStations();
    }

    @Override
    public boolean addRouteStop(TrainRoute route) throws RailwayException {
        if (route == null || route.getTrainId() <= 0 || route.getStationId() <= 0) {
            throw new RailwayException("Train ID and Station ID are required for route stop.");
        }
        return routeDAO.addRouteStop(route);
    }

    @Override
    public boolean updateRouteStop(TrainRoute route) throws RailwayException {
        if (route == null || route.getRouteId() <= 0) {
            throw new RailwayException("Invalid route record for update.");
        }
        return routeDAO.updateRouteStop(route);
    }

    @Override
    public boolean deleteRouteStop(int routeId) throws RailwayException {
        if (routeId <= 0) {
            throw new RailwayException("Invalid route ID.");
        }
        return routeDAO.deleteRouteStop(routeId);
    }

    @Override
    public List<TrainRoute> getRoutesByTrain(int trainId) {
        return routeDAO.getRoutesByTrainId(trainId);
    }

    @Override
    public boolean addCoach(TrainCoach coach) throws RailwayException {
        if (coach == null || coach.getTrainId() <= 0 || coach.getCoachNumber() == null) {
            throw new RailwayException("Train ID and coach number are required.");
        }
        return coachDAO.addCoach(coach);
    }

    @Override
    public boolean updateCoach(TrainCoach coach) throws RailwayException {
        if (coach == null || coach.getCoachId() <= 0) {
            throw new RailwayException("Invalid coach record for update.");
        }
        return coachDAO.updateCoach(coach);
    }

    @Override
    public boolean addSeatsToCoach(int coachId, int totalSeats, String defaultBerthDistribution) throws RailwayException {
        if (coachId <= 0 || totalSeats <= 0) {
            throw new RailwayException("Invalid coach ID or seat count.");
        }
        String[] berths = {"LB", "MB", "UB", "LB", "MB", "UB", "SL", "SU"};
        List<CoachSeat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            String berth = berths[(i - 1) % berths.length];
            seats.add(new CoachSeat(0, coachId, i, berth, true, null, null));
        }
        return seatDAO.addSeatsBatch(seats);
    }

    @Override
    public List<TrainCoach> getCoachesByTrain(int trainId) {
        return coachDAO.getCoachesByTrainId(trainId);
    }

    @Override
    public List<CoachSeat> getSeatsByCoach(int coachId) {
        return seatDAO.getSeatsByCoachId(coachId);
    }

    @Override
    public boolean addOrUpdateSchedule(TrainSchedule schedule) throws RailwayException {
        if (schedule == null || schedule.getTrainId() <= 0 || schedule.getJourneyDate() == null) {
            throw new RailwayException("Train ID and Journey Date are required for schedule.");
        }
        return scheduleDAO.addOrUpdateSchedule(schedule);
    }

    @Override
    public List<TrainSchedule> getSchedulesByTrain(int trainId) {
        return scheduleDAO.getSchedulesByTrain(trainId);
    }

    @Override
    public boolean addFare(TrainFare fare) throws RailwayException {
        if (fare == null || fare.getTrainId() <= 0 || fare.getFare() == null) {
            throw new RailwayException("Invalid fare details.");
        }
        return fareDAO.addFare(fare);
    }

    @Override
    public boolean updateFare(TrainFare fare) throws RailwayException {
        if (fare == null || fare.getFareId() <= 0) {
            throw new RailwayException("Invalid fare record for update.");
        }
        return fareDAO.updateFare(fare);
    }

    @Override
    public List<TrainFare> getFaresByTrain(int trainId) {
        return fareDAO.getFaresByTrain(trainId);
    }

    @Override
    public List<AuditLog> getAuditLogs(int limit) {
        return adminDAO.getAuditLogs(limit);
    }
}
