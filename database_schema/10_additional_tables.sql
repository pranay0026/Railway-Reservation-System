CREATE TABLE train_fare(
    fare_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id INT NOT NULL,
    coach_type_id INT NOT NULL,
    source_station_id INT NOT NULL,
    destination_station_id INT NOT NULL,
    fare DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_fare_train
    FOREIGN KEY(train_id)
    REFERENCES trains(train_id),

    CONSTRAINT fk_fare_coach_type
    FOREIGN KEY(coach_type_id)
    REFERENCES coach_type(coach_type_id),

    CONSTRAINT fk_fare_source
    FOREIGN KEY(source_station_id)
    REFERENCES stations(station_id),

    CONSTRAINT fk_fare_destination
    FOREIGN KEY(destination_station_id)
    REFERENCES stations(station_id),

    CONSTRAINT uq_train_fare
    UNIQUE(train_id, coach_type_id, source_station_id, destination_station_id)
);

CREATE TABLE train_availability(
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id INT NOT NULL,
    coach_id INT NOT NULL,
    journey_date DATE NOT NULL,
    available_seats INT DEFAULT 0,
    rac_count INT DEFAULT 0,
    waiting_count INT DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_availability_train
    FOREIGN KEY(train_id)
    REFERENCES trains(train_id),

    CONSTRAINT fk_availability_coach
    FOREIGN KEY(coach_id)
    REFERENCES train_coaches(coach_id),

    CONSTRAINT uq_availability
    UNIQUE(train_id, coach_id, journey_date)
);

CREATE TABLE audit_logs(
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(100) NOT NULL,
    record_id INT NOT NULL,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_admin
    FOREIGN KEY(admin_id)
    REFERENCES admins(admin_id)
);