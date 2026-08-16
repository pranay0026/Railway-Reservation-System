CREATE TABLE train_coaches(
    coach_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id INT NOT NULL,
    coach_type_id INT NOT NULL,
    coach_number VARCHAR(10) NOT NULL,
    total_seats INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_coach_train
    FOREIGN KEY(train_id)
    REFERENCES trains(train_id),

    CONSTRAINT fk_coach_type
    FOREIGN KEY(coach_type_id)
    REFERENCES coach_type(coach_type_id),

    CONSTRAINT uq_train_coach
    UNIQUE(train_id, coach_number)
);

CREATE TABLE coach_seats(
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    coach_id INT NOT NULL,
    seat_number INT NOT NULL,
    berth_type ENUM('LB','MB','UB','SL','SU') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_seat_coach
    FOREIGN KEY(coach_id)
    REFERENCES train_coaches(coach_id),

    CONSTRAINT uq_coach_seat
    UNIQUE(coach_id, seat_number)
);