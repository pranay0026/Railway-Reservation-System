package com.railway.dao;

import java.util.List;
import com.railway.model.Train;

public interface TrainDAO {
    boolean addTrain(Train train);
    boolean updateTrain(Train train);
    boolean setTrainActive(int trainId, boolean active);
    Train getTrainById(int trainId);
    Train getTrainByNumber(String trainNumber);
    List<Train> searchTrains(String query);
    List<Train> getAllTrains();
}
