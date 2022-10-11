drop database if exists Railway_Ticket_Office;

create database if not exists Railway_Ticket_Office;

USE Railway_Ticket_Office;

create table user_role(
                          id int primary key auto_increment,
                          user_role varchar(40)
);

create table user_gender(
                            id int primary key auto_increment,
                            user_gender varchar(40)
);

create table user_info(
                          id int primary key auto_increment,
                          username varchar(64) not null,
                          first_name varchar(64) not null,
                          last_name varchar(64) not null,
                          user_password varchar(516) not null,
                          user_count_of_money DECIMAL(17,2) default 0.00,
                          acount_verified boolean not null,
                          role_id int,
                          user_gender_id int not null,
                          user_phone varchar(40) unique,
                          user_email varchar(64) unique,
                          foreign key(role_id) references user_role(id),
                          foreign key(user_gender_id) references user_gender(id)
);

create table confirmation_token(
                                   id int primary key auto_increment,
                                   token varchar(256) not null,
                                   created_at datetime not null,
                                   expires_at datetime not null,
                                   user_id int not null,
                                   foreign key(user_id) references user_info(id)
);

create table cities(
                       id int primary key auto_increment,
                       city_name varchar(32) not null unique,
                       relevant boolean
);

create table station_list(
                             id int primary key auto_increment,
                             station_name varchar(248) not null,
                             city_id int not null,
                             relevant boolean,
                             foreign key(city_id) references cities(id)
);

create table train_info(
                           id int primary key auto_increment,
                           train_number varchar(20) not null,
                           number_of_compartment_seats int not null,
                           number_of_suite_seats int not null,
                           relevant boolean
);

create table route(
                      id int primary key auto_increment,
                      start_station_id int not null,
                      departure_time datetime not null,
                      travel_time time not null,
                      arrival_station_id int not null,
                      arrival_time datetime not null,
                      train_id int not null,
                      number_of_compartment_free_seats int not null,
                      number_of_suite_free_seats int not null,
                      prise_of_compartment_ticket decimal(8, 2) NOT NULL,
                      prise_of_suite_ticket decimal(8, 2) NOT NULL,
                      foreign key(start_station_id) references station_list(id),
                      foreign key(arrival_station_id) references station_list(id),
                      foreign key(train_id) references train_info(id)
);

create table user_comments(
                              id int primary key auto_increment,
                              user_comment varchar(5000),
                              publication_time datetime,
                              user_id int,
                              train_id int,
                              foreign key (user_id) references user_info(id),
                              foreign key (train_id) references train_info(id)
);

create table ticket_type(
                            id int primary key auto_increment,
                            ticket_type varchar(64),
                            ticket_price_factor FLOAT(3,2),
                            relevant boolean

);

create table purchased_tickets(
                                  id int primary key auto_increment,
                                  orders_prise decimal(8,2) not null,
                                  order_time datetime not null,
                                  count_of_purchased_tickets int not null,
                                  user_id int not null,
                                  route_id int not null,
                                  ticket_type_id int not null,
                                  foreign key (user_id) references user_info(id),
                                  foreign key (route_id) references route(id),
                                  foreign key (ticket_type_id) references ticket_type(id)
);

insert into cities(city_name,relevant)
    value ('Êè¿â',true),
    ('Ëüâ³â',true),
    ('Õàğê³â',true),
    ('Îäåñà',true);

insert into station_list(station_name,city_id,relevant) value
    ('Êè¿â Öåíòğ',1,true),('Êè¿â Ñõ³ä',1,true),('Êè¿â çàõ³ä',1,true),
    ('Õàğê³â Öåíòğ',3,true),('Îäåñà Öåíòğ',4,true),('Îäåñà Ñõ³ä',4,true),
    ('Õàğê³â Çàõ³ä',3,true),('Ëüâ³â Öåíòğ',2,true),('Ëüâ³â Ñõ³ä',2,true),
    ('Îäåñà Çàõ³ä',4,true);

insert into train_info(train_number,number_of_compartment_seats,number_of_suite_seats,relevant) value
    ('O-113',100,50,true),
    ('K-114',80,40,true),
    ('K-128',90,45,true);


insert into route(start_station_id,departure_time,travel_time,arrival_station_id,arrival_time,
                  train_id,number_of_compartment_free_seats, number_of_suite_free_seats, prise_of_compartment_ticket,prise_of_suite_ticket) value
    (1,'2022-08-23 02:30:09','05:30:00',4,'2022-08-23 02:30:09',1,100,20,800.00,1000.00),
    (1,'2022-08-23 02:30:09','07:30:00',8,'2022-08-23 02:30:09',2,70,30,700.00,1200.00),
    (1,'2022-08-23 02:30:09','06:00:00',9,'2022-08-23 02:30:09',3,80,40,900.00,1400.00);

insert into user_role(user_role) value
    ('USER'),('ADMIN');

insert into user_gender(user_gender) value
    ('MAN'),('WOMAN'),('NOT_SPECIFIED');

insert into ticket_type(ticket_type,ticket_price_factor,relevant) value
    ('Adult',1.00,true),('Children',0.85,true),('Pensioner',0.80,true),('Student',0.90,true);

insert into user_info(username,first_name,last_name,user_password,acount_verified, role_id, user_gender_id, user_phone, user_email)
    value
/*pass: testuser; testuser2; testadmin*/
    ('TestUser','Test','Test','$2a$12$Jfl1vcC0.f89P3W47CURTOL6ElJ0FPATc8PM7aaSQ72.K6EHBFj8O',true,1,1,'888-88-88-888','usertestemail@gmail.com'),
    ('TestUser2','Test2','Test2','$2a$12$iYBzrJzlfhAH4hVUc.q2Eed8jAR7bw2DqavkvOe8pUvxnNv1UCnmu',true,1,3,'000-00-00-000','usertest2email@gmail.com'),
    ('TestAdmin','Admin','Admin','$2a$12$PBiwbAao0twt5vwpV860J.Et/eKZb6ZHNhl62/iRSR8og/1E4Aq/C',true,2,1,'222-22-22-222','admintestemail@gmail.com');
