CREATE TABLE payments(
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_method_id INT NOT NULL,
    payment_status_id INT NOT NULL,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_booking
    FOREIGN KEY(booking_id)
    REFERENCES bookings(booking_id),

    CONSTRAINT fk_payment_method
    FOREIGN KEY(payment_method_id)
    REFERENCES payment_method(payment_method_id),

    CONSTRAINT fk_payment_status
    FOREIGN KEY(payment_status_id)
    REFERENCES payment_status(payment_status_id)
);

CREATE TABLE refunds(
    refund_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id INT NOT NULL,
    refund_amount DECIMAL(10,2) NOT NULL,
    refund_reason VARCHAR(255),
    refund_status ENUM('PENDING','PROCESSED','FAILED') DEFAULT 'PENDING',
    refund_date TIMESTAMP NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_refund_payment
    FOREIGN KEY(payment_id)
    REFERENCES payments(payment_id)
);