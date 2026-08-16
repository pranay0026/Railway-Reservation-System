package com.railway.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.railway.dao.BookingDAO;
import com.railway.model.Booking;
import com.railway.model.BookingPassenger;
import com.railway.model.Ticket;
import com.railway.util.DBConnection;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int createBooking(Connection conn, Booking booking) {
        String sql = "INSERT INTO bookings (pnr_number, user_id, train_id, source_station_id, destination_station_id, journey_date, total_passengers, total_fare, booking_status_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean localConn = false;
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
                localConn = true;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, booking.getPnrNumber());
                pstmt.setInt(2, booking.getUserId());
                pstmt.setInt(3, booking.getTrainId());
                pstmt.setInt(4, booking.getSourceStationId());
                pstmt.setInt(5, booking.getDestinationStationId());
                pstmt.setDate(6, booking.getJourneyDate());
                pstmt.setInt(7, booking.getTotalPassengers());
                pstmt.setBigDecimal(8, booking.getTotalFare());
                pstmt.setInt(9, booking.getBookingStatusId());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] createBooking error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public int addBookingPassenger(Connection conn, BookingPassenger passenger) {
        String sql = "INSERT INTO booking_passengers (booking_id, passenger_name, age, gender, berth_preference, seat_id, booking_status_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, passenger.getBookingId());
                pstmt.setString(2, passenger.getPassengerName());
                pstmt.setInt(3, passenger.getAge());
                pstmt.setString(4, passenger.getGender());
                pstmt.setString(5, passenger.getBerthPreference() != null ? passenger.getBerthPreference() : "NA");
                if (passenger.getSeatId() != null && passenger.getSeatId() > 0) {
                    pstmt.setInt(6, passenger.getSeatId());
                } else {
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                }
                pstmt.setInt(7, passenger.getBookingStatusId());

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] addBookingPassenger error: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean updateBookingStatus(Connection conn, int bookingId, int statusId) {
        String sql = "UPDATE bookings SET booking_status_id = ? WHERE booking_id = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, statusId);
                pstmt.setInt(2, bookingId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] updateBookingStatus error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updatePassengerStatusAndSeat(Connection conn, int passengerId, int statusId, Integer seatId) {
        String sql = "UPDATE booking_passengers SET booking_status_id = ?, seat_id = ? WHERE passenger_id = ?";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, statusId);
                if (seatId != null && seatId > 0) {
                    pstmt.setInt(2, seatId);
                } else {
                    pstmt.setNull(2, java.sql.Types.INTEGER);
                }
                pstmt.setInt(3, passengerId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] updatePassengerStatusAndSeat error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT b.booking_id, b.pnr_number, b.user_id, b.train_id, b.source_station_id, b.destination_station_id, " +
                     "b.journey_date, b.booking_date, b.total_passengers, b.total_fare, b.booking_status_id, b.created_at, b.updated_at, " +
                     "t.train_number, t.train_name, s1.station_name AS src_name, s1.station_code AS src_code, " +
                     "s2.station_name AS dst_name, s2.station_code AS dst_code, bs.status_name, u.full_name " +
                     "FROM bookings b " +
                     "JOIN trains t ON b.train_id = t.train_id " +
                     "JOIN stations s1 ON b.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON b.destination_station_id = s2.station_id " +
                     "JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE b.booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getBookingById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Booking getBookingByPNR(String pnr) {
        if (pnr == null) return null;
        String sql = "SELECT b.booking_id, b.pnr_number, b.user_id, b.train_id, b.source_station_id, b.destination_station_id, " +
                     "b.journey_date, b.booking_date, b.total_passengers, b.total_fare, b.booking_status_id, b.created_at, b.updated_at, " +
                     "t.train_number, t.train_name, s1.station_name AS src_name, s1.station_code AS src_code, " +
                     "s2.station_name AS dst_name, s2.station_code AS dst_code, bs.status_name, u.full_name " +
                     "FROM bookings b " +
                     "JOIN trains t ON b.train_id = t.train_id " +
                     "JOIN stations s1 ON b.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON b.destination_station_id = s2.station_id " +
                     "JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE UPPER(b.pnr_number) = UPPER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pnr.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getBookingByPNR error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.pnr_number, b.user_id, b.train_id, b.source_station_id, b.destination_station_id, " +
                     "b.journey_date, b.booking_date, b.total_passengers, b.total_fare, b.booking_status_id, b.created_at, b.updated_at, " +
                     "t.train_number, t.train_name, s1.station_name AS src_name, s1.station_code AS src_code, " +
                     "s2.station_name AS dst_name, s2.station_code AS dst_code, bs.status_name, u.full_name " +
                     "FROM bookings b " +
                     "JOIN trains t ON b.train_id = t.train_id " +
                     "JOIN stations s1 ON b.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON b.destination_station_id = s2.station_id " +
                     "JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE b.user_id = ? ORDER BY b.booking_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getBookingsByUserId error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<BookingPassenger> getPassengersByBookingId(int bookingId) {
        List<BookingPassenger> list = new ArrayList<>();
        String sql = "SELECT bp.passenger_id, bp.booking_id, bp.passenger_name, bp.age, bp.gender, bp.berth_preference, " +
                     "bp.seat_id, bp.booking_status_id, bp.created_at, bp.updated_at, " +
                     "bs.status_name, s.seat_number, s.berth_type, c.coach_number, " +
                     "r.rac_number, w.waitlist_number " +
                     "FROM booking_passengers bp " +
                     "JOIN booking_status bs ON bp.booking_status_id = bs.booking_status_id " +
                     "LEFT JOIN coach_seats s ON bp.seat_id = s.seat_id " +
                     "LEFT JOIN train_coaches c ON s.coach_id = c.coach_id " +
                     "LEFT JOIN rac r ON bp.passenger_id = r.passenger_id " +
                     "LEFT JOIN waitlist w ON bp.passenger_id = w.passenger_id " +
                     "WHERE bp.booking_id = ? ORDER BY bp.passenger_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BookingPassenger p = new BookingPassenger(
                            rs.getInt("passenger_id"),
                            rs.getInt("booking_id"),
                            rs.getString("passenger_name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("berth_preference"),
                            rs.getInt("seat_id"),
                            rs.getInt("booking_status_id"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    if (rs.wasNull()) {
                        p.setSeatId(null);
                    }
                    p.setStatusName(rs.getString("status_name"));
                    p.setCoachNumber(rs.getString("coach_number"));
                    int seatNum = rs.getInt("seat_number");
                    p.setSeatNumber(rs.wasNull() ? null : seatNum);
                    p.setBerthType(rs.getString("berth_type"));

                    int racNum = rs.getInt("rac_number");
                    p.setRacNumber(rs.wasNull() ? null : racNum);

                    int wlNum = rs.getInt("waitlist_number");
                    p.setWaitlistNumber(rs.wasNull() ? null : wlNum);

                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getPassengersByBookingId error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean createTicket(Connection conn, Ticket ticket) {
        String sql = "INSERT INTO tickets (booking_id, ticket_number, qr_code, ticket_pdf) VALUES (?, ?, ?, ?)";
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnection.getConnection();
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, ticket.getBookingId());
                pstmt.setString(2, ticket.getTicketNumber());
                pstmt.setString(3, ticket.getQrCode());
                pstmt.setString(4, ticket.getTicketPdf());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] createTicket error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Ticket getTicketByBookingId(int bookingId) {
        String sql = "SELECT ticket_id, booking_id, ticket_number, issue_date, qr_code, ticket_pdf, created_at, updated_at " +
                     "FROM tickets WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("ticket_id"),
                            rs.getInt("booking_id"),
                            rs.getString("ticket_number"),
                            rs.getTimestamp("issue_date"),
                            rs.getString("qr_code"),
                            rs.getString("ticket_pdf"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getTicketByBookingId error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.pnr_number, b.user_id, b.train_id, b.source_station_id, b.destination_station_id, " +
                     "b.journey_date, b.booking_date, b.total_passengers, b.total_fare, b.booking_status_id, b.created_at, b.updated_at, " +
                     "t.train_number, t.train_name, s1.station_name AS src_name, s1.station_code AS src_code, " +
                     "s2.station_name AS dst_name, s2.station_code AS dst_code, bs.status_name, u.full_name " +
                     "FROM bookings b " +
                     "JOIN trains t ON b.train_id = t.train_id " +
                     "JOIN stations s1 ON b.source_station_id = s1.station_id " +
                     "JOIN stations s2 ON b.destination_station_id = s2.station_id " +
                     "JOIN booking_status bs ON b.booking_status_id = bs.booking_status_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "ORDER BY b.booking_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("[BookingDAO] getAllBookings error: " + e.getMessage());
        }
        return list;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setPnrNumber(rs.getString("pnr_number"));
        b.setUserId(rs.getInt("user_id"));
        b.setTrainId(rs.getInt("train_id"));
        b.setSourceStationId(rs.getInt("source_station_id"));
        b.setDestinationStationId(rs.getInt("destination_station_id"));
        b.setJourneyDate(rs.getDate("journey_date"));
        b.setBookingDate(rs.getTimestamp("booking_date"));
        b.setTotalPassengers(rs.getInt("total_passengers"));
        b.setTotalFare(rs.getBigDecimal("total_fare"));
        b.setBookingStatusId(rs.getInt("booking_status_id"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.setUpdatedAt(rs.getTimestamp("updated_at"));
        b.setTrainNumber(rs.getString("train_number"));
        b.setTrainName(rs.getString("train_name"));
        b.setSourceStationName(rs.getString("src_name"));
        b.setSourceStationCode(rs.getString("src_code"));
        b.setDestinationStationName(rs.getString("dst_name"));
        b.setDestinationStationCode(rs.getString("dst_code"));
        b.setStatusName(rs.getString("status_name"));
        b.setUserFullName(rs.getString("full_name"));
        return b;
    }
}
