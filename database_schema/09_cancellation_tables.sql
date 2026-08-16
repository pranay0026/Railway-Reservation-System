CREATE TABLE cancellations(
    cancellation_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    cancelled_by INT NOT NULL,
    cancellation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cancellation_reason VARCHAR(255),
    cancellation_charge DECIMAL(10,2) DEFAULT 0.00,
    refund_amount DECIMAL(10,2) DEFAULT 0.00,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cancel_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id),

    CONSTRAINT fk_cancel_user
    FOREIGN KEY(cancelled_by)
    REFERENCES users(user_id)
);

CREATE TABLE waitlist(
    waitlist_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_id INT NOT NULL,
    booking_id INT NOT NULL,
    waitlist_number INT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_waitlist_passenger
    FOREIGN KEY(passenger_id)
    REFERENCES booking_passengers(passenger_id),

    CONSTRAINT fk_waitlist_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id),

    CONSTRAINT uq_waitlist
    UNIQUE(booking_id, waitlist_number)
);

CREATE TABLE rac(
    rac_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_id INT NOT NULL,
    booking_id INT NOT NULL,
    rac_number INT NOT NULL,
    seat_id INT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_rac_passenger
    FOREIGN KEY(passenger_id)
    REFERENCES booking_passengers(passenger_id),

    CONSTRAINT fk_rac_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id),

    CONSTRAINT fk_rac_seat
    FOREIGN KEY(seat_id)
    REFERENCES coach_seats(seat_id),

    CONSTRAINT uq_rac
    UNIQUE(booking_id, rac_number)
);