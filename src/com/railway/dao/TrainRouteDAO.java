package com.railway.dao;

import java.util.List;
import com.railway.model.Train;
import com.railway.model.TrainRoute;

public interface TrainRouteDAO {
    boolean addRouteStop(TrainRoute route);
    boolean updateRouteStop(TrainRoute route);
    boolean deleteRouteStop(int routeId);
    List<TrainRoute> getRoutesByTrainId(int trainId);
    TrainRoute getRouteStop(int trainId, int stationId);
    List<Train> findTrainsBetweenStations(int sourceStationId, int destinationStationId);
}
