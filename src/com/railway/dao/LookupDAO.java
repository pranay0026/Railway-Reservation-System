package com.railway.dao;

import java.util.List;
import java.util.Map;
import com.railway.model.BookingStatus;
import com.railway.model.CoachType;
import com.railway.model.PaymentMethod;
import com.railway.model.PaymentStatus;
import com.railway.model.TrainType;

public interface LookupDAO {
    int getBookingStatusId(String statusName);
    String getBookingStatusName(int id);

    int getPaymentStatusId(String statusName);
    String getPaymentStatusName(int id);

    int getPaymentMethodId(String methodName);
    String getPaymentMethodName(int id);

    int getCoachTypeId(String coachName);
    String getCoachTypeName(int id);

    List<CoachType> getAllCoachTypes();
    List<TrainType> getAllTrainTypes();
    List<PaymentMethod> getAllPaymentMethods();
    List<PaymentStatus> getAllPaymentStatuses();
    List<BookingStatus> getAllBookingStatuses();
}
