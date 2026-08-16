package com.railway.dao;

import java.util.List;
import com.railway.model.Station;

public interface StationDAO {
    boolean addStation(Station station);
    boolean updateStation(Station station);
    Station getStationById(int stationId);
    Station getStationByCode(String code);
    List<Station> searchStations(String query);
    List<Station> getAllStations();
}
