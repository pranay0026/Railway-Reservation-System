package com.railway.dao;

import java.sql.Date;
import java.util.List;
import com.railway.model.CoachSeat;

public interface SeatDAO {
    boolean addSeat(CoachSeat seat);
    boolean addSeatsBatch(List<CoachSeat> seats);
    boolean updateSeat(CoachSeat seat);
    CoachSeat getSeatById(int seatId);
    List<CoachSeat> getSeatsByCoachId(int coachId);
    List<CoachSeat> getAvailableSeatsForCoach(int coachId, Date journeyDate);
    List<CoachSeat> getAvailableSeatsForTrainAndType(int trainId, int coachTypeId, Date journeyDate);
    List<Integer> getBookedSeatIds(int trainId, Date journeyDate);
}
