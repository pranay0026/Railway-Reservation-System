package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.railway.dao.LookupDAO;
import com.railway.model.BookingStatus;
import com.railway.model.CoachType;
import com.railway.model.PaymentMethod;
import com.railway.model.PaymentStatus;
import com.railway.model.TrainType;
import com.railway.util.DBConnection;

public class LookupDAOImpl implements LookupDAO {

    @Override
    public int getBookingStatusId(String statusName) {
        String sql = "SELECT booking_status_id FROM booking_status WHERE UPPER(status_name) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("booking_status_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getBookingStatusId error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public String getBookingStatusName(int id) {
        String sql = "SELECT status_name FROM booking_status WHERE booking_status_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getBookingStatusName error: " + e.getMessage());
        }
        return "UNKNOWN";
    }

    @Override
    public int getPaymentStatusId(String statusName) {
        String sql = "SELECT payment_status_id FROM payment_status WHERE UPPER(status_name) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("payment_status_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getPaymentStatusId error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public String getPaymentStatusName(int id) {
        String sql = "SELECT status_name FROM payment_status WHERE payment_status_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getPaymentStatusName error: " + e.getMessage());
        }
        return "UNKNOWN";
    }

    @Override
    public int getPaymentMethodId(String methodName) {
        String sql = "SELECT payment_method_id FROM payment_method WHERE UPPER(method_name) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, methodName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("payment_method_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getPaymentMethodId error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public String getPaymentMethodName(int id) {
        String sql = "SELECT method_name FROM payment_method WHERE payment_method_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("method_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getPaymentMethodName error: " + e.getMessage());
        }
        return "UNKNOWN";
    }

    @Override
    public int getCoachTypeId(String coachName) {
        String sql = "SELECT coach_type_id FROM coach_type WHERE UPPER(coach_name) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coachName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("coach_type_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getCoachTypeId error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public String getCoachTypeName(int id) {
        String sql = "SELECT coach_name FROM coach_type WHERE coach_type_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("coach_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getCoachTypeName error: " + e.getMessage());
        }
        return "UNKNOWN";
    }

    @Override
    public List<CoachType> getAllCoachTypes() {
        List<CoachType> list = new ArrayList<>();
        String sql = "SELECT coach_type_id, coach_name, description FROM coach_type ORDER BY coach_type_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CoachType(
                        rs.getInt("coach_type_id"),
                        rs.getString("coach_name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getAllCoachTypes error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<TrainType> getAllTrainTypes() {
        List<TrainType> list = new ArrayList<>();
        String sql = "SELECT train_type_id, type_name, description FROM train_types ORDER BY train_type_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TrainType(
                        rs.getInt("train_type_id"),
                        rs.getString("type_name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getAllTrainTypes error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> list = new ArrayList<>();
        String sql = "SELECT payment_method_id, method_name FROM payment_method ORDER BY payment_method_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PaymentMethod(
                        rs.getInt("payment_method_id"),
                        rs.getString("method_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getAllPaymentMethods error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<PaymentStatus> getAllPaymentStatuses() {
        List<PaymentStatus> list = new ArrayList<>();
        String sql = "SELECT payment_status_id, status_name FROM payment_status ORDER BY payment_status_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PaymentStatus(
                        rs.getInt("payment_status_id"),
                        rs.getString("status_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getAllPaymentStatuses error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<BookingStatus> getAllBookingStatuses() {
        List<BookingStatus> list = new ArrayList<>();
        String sql = "SELECT booking_status_id, status_name, description FROM booking_status ORDER BY booking_status_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new BookingStatus(
                        rs.getInt("booking_status_id"),
                        rs.getString("status_name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LookupDAO] getAllBookingStatuses error: " + e.getMessage());
        }
        return list;
    }
}
