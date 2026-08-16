INSERT INTO booking_status(status_name,description)
VALUES
('CONFIRMED','Seat allotted'),
('WAITLIST','Waiting for seat'),
('RAC','Reservation Against Cancellation'),
('CANCELLED','Ticket cancelled');

INSERT INTO payment_status(status_name)
VALUES
('SUCCESS'),
('FAILED'),
('PENDING'),
('REFUNDED');

INSERT INTO payment_method(method_name)
VALUES
('UPI'),
('Credit Card'),
('Debit Card'),
('Net Banking');

INSERT INTO coach_type(coach_name,description)
VALUES
('SL','Sleeper'),
('3A','AC Three Tier'),
('2A','AC Two Tier'),
('1A','First AC'),
('CC','Chair Car'),
('2S','Second Sitting');

INSERT INTO train_types(type_name, description)
VALUES
('Express','Regular Express Train'),
('Superfast','High Speed Superfast Train'),
('Rajdhani','Premium Rajdhani Express'),
('Shatabdi','Daytime Premium Express'),
('Duronto','Non-stop Premium Train'),
('Garib Rath','Affordable AC Express'),
('Passenger','Passenger Train'),
('MEMU','Mainline Electric Multiple Unit'),
('DEMU','Diesel Electric Multiple Unit'),
('Vande Bharat','Semi High Speed Train');