package com.railway.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.railway.dao.AvailabilityDAO;
import com.railway.dao.CoachDAO;
import com.railway.dao.FareDAO;
import com.railway.dao.LookupDAO;
import com.railway.dao.SeatDAO;
import com.railway.dao.StationDAO;
import com.railway.dao.TrainDAO;
import com.railway.dao.TrainRouteDAO;
import com.railway.dao.TrainScheduleDAO;
import com.railway.dao.impl.AvailabilityDAOImpl;
import com.railway.dao.impl.CoachDAOImpl;
import com.railway.dao.impl.FareDAOImpl;
import com.railway.dao.impl.LookupDAOImpl;
import com.railway.dao.impl.SeatDAOImpl;
import com.railway.dao.impl.StationDAOImpl;
import com.railway.dao.impl.TrainDAOImpl;
import com.railway.dao.impl.TrainRouteDAOImpl;
import com.railway.dao.impl.TrainScheduleDAOImpl;
import com.railway.exception.RailwayException;
import com.railway.model.CoachSeat;
import com.railway.model.CoachType;
import com.railway.model.Station;
import com.railway.model.Train;
import com.railway.model.TrainCoach;
import com.railway.model.TrainRoute;
import com.railway.model.TrainSchedule;
import com.railway.service.TrainService;

public class TrainServiceImpl implements TrainService {
    private final TrainDAO trainDAO;
    private final TrainRouteDAO routeDAO;
    private final StationDAO stationDAO;
    private final TrainScheduleDAO scheduleDAO;
    private final CoachDAO coachDAO;
    private final SeatDAO seatDAO;
    private final AvailabilityDAO availabilityDAO;
    private final FareDAO fareDAO;
    private final LookupDAO lookupDAO;

    public TrainServiceImpl() {
        this.trainDAO = new TrainDAOImpl();
        this.routeDAO = new TrainRouteDAOImpl();
        this.stationDAO = new StationDAOImpl();
        this.scheduleDAO = new TrainScheduleDAOImpl();
        this.coachDAO = new CoachDAOImpl();
        this.seatDAO = new SeatDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.fareDAO = new FareDAOImpl();
        this.lookupDAO = new LookupDAOImpl();
    }

    @Override
    public List<Train> searchTrains(String sourceCode, String destCode, LocalDate journeyDate) throws RailwayException {
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            throw new RailwayException("Source station code is required.");
        }
        if (destCode == null || destCode.trim().isEmpty()) {
            throw new RailwayException("Destination station code is required.");
        }

        Station src = stationDAO.getStationByCode(sourceCode.trim());
        if (src == null) {
            throw new RailwayException("Source station not found: " + sourceCode);
        }

        Station dst = stationDAO.getStationByCode(destCode.trim());
        if (dst == null) {
            throw new RailwayException("Destination station not found: " + destCode);
        }

        if (src.getStationId() == dst.getStationId()) {
            throw new RailwayException("Source and Destination stations cannot be the same.");
        }

        if (journeyDate == null || journeyDate.isBefore(LocalDate.now())) {
            throw new RailwayException("Journey date must be today or in the future.");
        }

        // Find trains where route order is correct
        List<Train> matchingTrains = routeDAO.findTrainsBetweenStations(src.getStationId(), dst.getStationId());
        List<Train> availableTrains = new ArrayList<>();
        Date sqlDate = Date.valueOf(journeyDate);

        for (Train train : matchingTrains) {
            // Check schedule if present
            TrainSchedule schedule = scheduleDAO.getSchedule(train.getTrainId(), sqlDate);
            if (schedule != null && "Cancelled".equalsIgnoreCase(schedule.getRunningStatus())) {
                continue; // Train cancelled on this day
            }
            availableTrains.add(train);
        }

        return availableTrains;
    }

    @Override
    public List<TrainCoach> getCoachesByTrain(int trainId) {
        return coachDAO.getCoachesByTrainId(trainId);
    }

    @Override
    public List<CoachType> getAllCoachTypes() {
        return lookupDAO.getAllCoachTypes();
    }

    @Override
    public List<CoachSeat> getAvailableSeats(int trainId, int coachTypeId, LocalDate journeyDate) {
        return seatDAO.getAvailableSeatsForTrainAndType(trainId, coachTypeId, Date.valueOf(journeyDate));
    }

    @Override
    public int getAvailableSeatsCount(int trainId, int coachTypeId, LocalDate journeyDate) {
        List<CoachSeat> seats = getAvailableSeats(trainId, coachTypeId, journeyDate);
        return seats.size();
    }

    @Override
    public Map<String, Integer> getSeatAvailabilityByClass(int trainId, LocalDate journeyDate) {
        Map<String, Integer> map = new LinkedHashMap<>();
        List<CoachType> coachTypes = lookupDAO.getAllCoachTypes();
        for (CoachType ct : coachTypes) {
            List<TrainCoach> coaches = coachDAO.getCoachesByTrainAndType(trainId, ct.getCoachTypeId());
            if (!coaches.isEmpty()) {
                int count = getAvailableSeatsCount(trainId, ct.getCoachTypeId(), journeyDate);
                map.put(ct.getCoachName(), count);
            }
        }
        return map;
    }

    @Override
    public BigDecimal getFare(int trainId, int coachTypeId, int sourceStationId, int destinationStationId) {
        return fareDAO.calculateFare(trainId, coachTypeId, sourceStationId, destinationStationId);
    }

    @Override
    public Train getTrainById(int trainId) {
        return trainDAO.getTrainById(trainId);
    }

    @Override
    public Train getTrainByNumber(String trainNumber) {
        return trainDAO.getTrainByNumber(trainNumber);
    }

    @Override
    public Station getStationByCode(String code) {
        return stationDAO.getStationByCode(code);
    }

    @Override
    public Station getStationById(int stationId) {
        return stationDAO.getStationById(stationId);
    }

    @Override
    public List<Station> searchStations(String query) {
        return stationDAO.searchStations(query);
    }

    @Override
    public List<Station> getAllStations() {
        return stationDAO.getAllStations();
    }

    @Override
    public List<TrainRoute> getTrainRoute(int trainId) {
        return routeDAO.getRoutesByTrainId(trainId);
    }

    @Override
    public TrainSchedule getSchedule(int trainId, LocalDate journeyDate) {
        return scheduleDAO.getSchedule(trainId, Date.valueOf(journeyDate));
    }
}
