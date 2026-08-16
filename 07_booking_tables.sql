CREATE TABLE bookings(
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    pnr_number VARCHAR(20) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    source_station_id INT NOT NULL,
    destination_station_id INT NOT NULL,
    journey_date DATE NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_passengers INT NOT NULL,
    total_fare DECIMAL(10,2) NOT NULL,
    booking_status_id INT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user
    FOREIGN KEY(user_id)
    REFERENCES users(user_id),

    CONSTRAINT fk_booking_train
    FOREIGN KEY(train_id)
    REFERENCES trains(train_id),

    CONSTRAINT fk_booking_source
    FOREIGN KEY(source_station_id)
    REFERENCES stations(station_id),

    CONSTRAINT fk_booking_destination
    FOREIGN KEY(destination_station_id)
    REFERENCES stations(station_id),

    CONSTRAINT fk_booking_status
    FOREIGN KEY(booking_status_id)
    REFERENCES booking_status(booking_status_id)
);

CREATE TABLE booking_passengers(
    passenger_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male','Female','Other') NOT NULL,
    berth_preference ENUM('LB','MB','UB','SL','SU','NA') DEFAULT 'NA',

    seat_id INT,
    booking_status_id INT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_passenger_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id),

    CONSTRAINT fk_passenger_seat
    FOREIGN KEY(seat_id)
    REFERENCES coach_seats(seat_id),

    CONSTRAINT fk_passenger_status
    FOREIGN KEY(booking_status_id)
    REFERENCES booking_status(booking_status_id)
);

CREATE TABLE tickets(
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    ticket_number VARCHAR(30) NOT NULL UNIQUE,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    qr_code VARCHAR(255),
    ticket_pdf VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_ticket_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id)
);