CREATE VIEW booking_details AS
SELECT
    b.booking_id,
    b.pnr_number,
    u.full_name,
    t.train_number,
    t.train_name,
    s1.station_name AS source_station,
    s2.station_name AS destination_station,
    b.journey_date,
    bs.status_name
FROM bookings b
JOIN users u
ON b.user_id=u.user_id
JOIN trains t
ON b.train_id=t.train_id
JOIN stations s1
ON b.source_station_id=s1.station_id
JOIN stations s2
ON b.destination_station_id=s2.station_id
JOIN booking_status bs
ON b.booking_status_id=bs.booking_status_id;


CREATE VIEW train_route_view AS
SELECT
t.train_number,
t.train_name,
s.station_name,
tr.stop_number,
tr.arrival_time,
tr.departure_time
FROM train_routes tr
JOIN trains t
ON tr.train_id=t.train_id
JOIN stations s
ON tr.station_id=s.station_id;