package com.railway.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import com.railway.model.Cancellation;
import com.railway.model.RAC;
import com.railway.model.Waitlist;

public interface CancellationDAO {
    int recordCancellation(Connection conn, Cancellation cancellation);
    Cancellation getCancellationByBookingId(int bookingId);
    int addToWaitlist(Connection conn, Waitlist waitlist);
    int addToRAC(Connection conn, RAC rac);
    RAC getNextRACPassenger(Connection conn, int trainId, Date journeyDate);
    Waitlist getNextWaitlistPassenger(Connection conn, int trainId, Date journeyDate);
    boolean removeRAC(Connection conn, int racId);
    boolean removeWaitlist(Connection conn, int waitlistId);
    List<RAC> getRACQueueForTrainAndDate(int trainId, Date journeyDate);
    List<Waitlist> getWaitlistQueueForTrainAndDate(int trainId, Date journeyDate);
    int getNextRACNumber(Connection conn, int trainId, Date journeyDate);
    int getNextWaitlistNumber(Connection conn, int trainId, Date journeyDate);
    List<Cancellation> getAllCancellations();
}
