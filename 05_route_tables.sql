
create table trains(
train_id int auto_increment primary key,
train_number varchar(10) not null unique,
train_name varchar(100) not null,
train_type_id int not null,
total_distance int,
is_active boolean default true,
created_at timestamp default current_timestamp,
updated_at timestamp default current_timestamp on update current_timestamp,
constraint fk_train_type
foreign key(train_type_id)
references train_types(train_type_id));

create table train_routes(
route_id int Auto_increment primary key,
train_id int not null,
station_id int not null,
stop_number int not null,
arrival_time time,
departure_time time,
distance_from_source int not null,
platform_number varchar(5),
constraint fk_route_train foreign key(train_id) references
trains(train_id) on delete cascade,
constraint fk_route_station foreign key(station_id) references
stations(station_id),
constraint uk_train_stop unique(train_id,stop_number),
constraint uk_train_station unique(train_id,station_id)
);

create table train_schedule(
schedule_id int auto_increment primary key,
train_id int not null,
journey_date date not null,
running_status Enum('Running','Cancelled','Delayed') default 'Running',
delay_minutes int default 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT fk_schedule_train
FOREIGN KEY(train_id)
REFERENCES trains(train_id),
CONSTRAINT uq_train_date
UNIQUE(train_id, journey_date));



