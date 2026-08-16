-- Trains
CREATE INDEX idx_train_number
ON trains(train_number);

-- Stations
CREATE INDEX idx_station_code
ON stations(station_code);

-- Routes
CREATE INDEX idx_route_train
ON train_routes(train_id);

CREATE INDEX idx_route_station
ON train_routes(station_id);

-- Bookings
CREATE INDEX idx_booking_pnr
ON bookings(pnr_number);

CREATE INDEX idx_booking_user
ON bookings(user_id);

CREATE INDEX idx_booking_date
ON bookings(journey_date);

-- Payments
CREATE INDEX idx_payment_transaction
ON payments(transaction_id);

-- Availability
CREATE INDEX idx_train_availability
ON train_availability(train_id, journey_date);