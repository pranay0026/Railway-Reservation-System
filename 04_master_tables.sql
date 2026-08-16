create table admins(
admin_id int auto_increment primary key,
username varchar(50) not null,
password varchar(255) not null,
full_name varchar(100),
role varchar(30) default 'Admin',
created_at timestamp default current_timestamp,
constraint uk_admin_username unique(username));

create table stations(
station_id int auto_increment,
station_code varchar(10) not null unique,
station_name varchar(100) not null,
city varchar(100) not null,
state varchar(100) not null,
zone varchar(50),
create_at timestamp default current_timestamp,
primary key(station_id));

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    gender ENUM('Male','Female','Other'),
    dob DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_email UNIQUE(email),
    CONSTRAINT uk_user_phone UNIQUE(phone)
);

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