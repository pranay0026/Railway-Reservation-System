
Create table booking_status(
booking_status_id int auto_increment,
status_name varchar(30) Not null unique,
description varchar(100),
primary key(booking_status_id)
);


create table payment_status(
payment_status_id int auto_increment,
status_name varchar(30) not null unique,
primary key(payment_status_id)
);


create table payment_method(
payment_method_id int auto_increment,
method_name varchar(30) unique not null,
primary key(payment_method_id)
);


create table coach_type(
coach_type_id int auto_increment,
coach_name varchar(30) unique not null,
description varchar(100),
primary key(coach_type_id));

create table train_types(
train_type_id int auto_increment primary key,
type_name varchar(50) not null unique,
description varchar(150));





