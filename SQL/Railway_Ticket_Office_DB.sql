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
                          foreign key(role_id) references user_role(id) on delete cascade ,
                          foreign key(user_gender_id) references user_gender(id) on delete cascade
);

create table confirmation_token(
                                   id int primary key auto_increment,
                                   token varchar(256) not null,
                                   created_at datetime not null,
                                   expires_at datetime not null,
                                   user_id int not null,
                                   foreign key(user_id) references user_info(id)
);

create table city(
                     id int primary key auto_increment,
                     created timestamp not null,
                     updated timestamp not null,
                     created_by varchar(64)not null,
                     last_modified_by varchar(64)not null,
                     city_name varchar(32) not null unique,
                     relevant boolean
);

create table station(
                        id int primary key auto_increment,
                        created timestamp not null,
                        updated timestamp not null,
                        created_by varchar(64)not null,
                        last_modified_by varchar(64)not null,
                        station_name varchar(248) not null,
                        city_id int not null,
                        relevant boolean,
                        foreign key(city_id) references city(id) on delete cascade
);

create table train_info(
                           id int primary key auto_increment,
                           created timestamp not null,
                           updated timestamp not null,
                           created_by varchar(64)not null,
                           last_modified_by varchar(64)not null,
                           train_number varchar(20) not null,
                           number_of_compartment_seats int not null,
                           number_of_suite_seats int not null,
                           relevant boolean
);

create table route(
                      id int primary key auto_increment,
                      created timestamp not null,
                      updated timestamp not null,
                      created_by varchar(64)not null,
                      last_modified_by varchar(64)not null,
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
                      foreign key(start_station_id) references station(id) on delete cascade ,
                      foreign key(arrival_station_id) references station(id) on delete cascade ,
                      foreign key(train_id) references train_info(id) on delete cascade
);

create table intermediate_stations(
                                      route_id int not null,
                                      station_id int not null,
                                      foreign key(route_id) references route(id) on delete cascade ,
                                      foreign key(station_id) references station(id) on delete cascade
);

create table user_comments(
                              id int primary key auto_increment,
                              created timestamp not null,
                              updated timestamp not null,
                              created_by varchar(64)not null,
                              last_modified_by varchar(64)not null,
                              user_comment varchar(5000),
                              user_id int,
                              train_id int,
                              foreign key (user_id) references user_info(id),
                              foreign key (train_id) references train_info(id)
);

create table ticket_type(
                            id int primary key auto_increment,
                            created timestamp not null,
                            updated timestamp not null,
                            created_by varchar(64)not null,
                            last_modified_by varchar(64)not null,
                            ticket_type varchar(64),
                            ticket_price_factor FLOAT(3,2),
                            relevant boolean

);

create table purchased_tickets(
                                  id int primary key auto_increment,
                                  created timestamp not null,
                                  updated timestamp not null,
                                  created_by varchar(64)not null,
                                  last_modified_by varchar(64)not null,
                                  orders_prise decimal(8,2) not null,
                                  user_id int not null,
                                  route_id int not null,
                                  foreign key (user_id) references user_info(id),
                                  foreign key (route_id) references route(id)
);

create table place(
                      id int primary key auto_increment,
                      place varchar(64)not null
);

create table tickets_count(
                              id int primary key auto_increment,
                              created timestamp not null,
                              updated timestamp not null,
                              created_by varchar(64)not null,
                              last_modified_by varchar(64)not null,
                              count_of_purchased_tickets int not null,
                              ticket_type_id int not null,
                              purchased_tickets_id int not null,
                              place_id int not null,
                              foreign key (ticket_type_id) references ticket_type(id),
                              foreign key (purchased_tickets_id) references purchased_tickets(id),
                              foreign key (place_id) references place(id)
);


insert into city(created_by, last_modified_by, created, updated, city_name, relevant)
    value
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',' ињв',true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Ћьв≥в',true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’арк≥в',true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','ќдеса',true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','∆итомир',true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’ерсон',true);

insert into station(created_by, last_modified_by, created, updated, station_name, city_id, relevant)
    value
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',' ињв ÷ентр',1,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',' ињв —х≥д',1,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',' ињв зах≥д',1,true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Ћьв≥в ÷ентр',2,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Ћьв≥в —х≥д',2,true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’арк≥в ÷ентр',3,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’арк≥в «ах≥д',3,true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','ќдеса ÷ентр',4,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','ќдеса —х≥д',4,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','ќдеса «ах≥д',4,true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','∆итомир ÷ентир',5,true),

    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’ерсон ÷ентир',6,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','’ерсон —х≥д',6,true);

insert into train_info(created_by, last_modified_by, created, updated, train_number, number_of_compartment_seats, number_of_suite_seats,relevant)
    value
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','O-113',100,50,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','K-114',80,40,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','K-128',270,100,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','P-88',185,75,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','K-128',90,45,true);


insert into route(created_by, last_modified_by, created,updated, start_station_id, departure_time, travel_time,
                  arrival_station_id, arrival_time, train_id, number_of_compartment_free_seats, number_of_suite_free_seats,
                  prise_of_compartment_ticket, prise_of_suite_ticket)
    value
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',1,'2022-11-27 19:15:06','05:30:00',4,'2022-11-28 02:30:09',1,100,50,800.00,1000.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',1,'2022-11-27 19:15:06','07:30:00',8,'2022-11-28 02:30:09',2,70,30,700.00,1200.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',5,'2022-11-27 19:15:06','07:30:00',6,'2022-11-28 02:30:09',3,270,100,850.00,1350.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',5,'2022-11-27 19:15:06','07:30:00',3,'2022-11-28 02:30:09',4,185,75,900.00,1400.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',10,'2022-11-27 19:15:06','07:30:00',1,'2022-11-28 02:30:09',5,90,45,650.00,1100.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',7,'2022-11-24 19:15:06','07:30:00',4,'2022-11-25 02:30:09',3,270,100,550.00,950.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',2,'2022-11-25 19:15:06','07:30:00',9,'2022-11-26 02:30:09',2,80,40,770.00,1340.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',4,'2022-11-23 19:15:06','07:30:00',7,'2022-11-24 02:30:09',5,90,45,880.00,1220.00),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17',1,'2022-11-27 19:15:06','06:00:00',9,'2022-11-28 02:30:09',3,80,40,900.00,1400.00);

insert into intermediate_stations(route_id, station_id)
    value
    (1,11),(1,12),(2,13),(3,11),(3,2),(4,11),
    (5,12),(6,3),(6,11),(7,12),(8,11),(8,1),
    (9,13);

insert into user_role(user_role)
    value
    ('USER'),('ADMIN');

insert into user_gender(user_gender)
    value
    ('MAN'),('WOMAN'),('NOT_SPECIFIED');

insert into place(place)
    value
    ('COMPARTMENT'),('SUITE');

insert into ticket_type(created_by, last_modified_by, created, updated, ticket_type, ticket_price_factor, relevant)
    value
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Adult',1.00,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Children',0.85,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Pensioner',0.80,true),
    ('TestAdmin','TestAdmin','2022-11-21 20:59:17','2022-11-21 20:59:17','Student',0.90,true);

insert into user_info(username, first_name, last_name, user_password, acount_verified, role_id, user_gender_id, user_phone, user_email)
    value
/*pass: testuser; testuser2; testadmin*/
    ('TestUser','Test','Test','$2a$12$Jfl1vcC0.f89P3W47CURTOL6ElJ0FPATc8PM7aaSQ72.K6EHBFj8O',true,1,1,'888-88-88-888','usertestemail@gmail.com'),
    ('TestUser2','Test2','Test2','$2a$12$iYBzrJzlfhAH4hVUc.q2Eed8jAR7bw2DqavkvOe8pUvxnNv1UCnmu',true,1,3,'000-00-00-000','usertest2email@gmail.com'),
    ('TestAdmin','Admin','Admin','$2a$12$PBiwbAao0twt5vwpV860J.Et/eKZb6ZHNhl62/iRSR8og/1E4Aq/C',true,2,1,'222-22-22-222','admintestemail@gmail.com');

insert into user_comments(created_by, last_modified_by, created, updated, user_comment, user_id, train_id)
    value
    ('TestUser','TestUser','2022-11-21 20:59:17','2022-11-21 20:59:17','Test comment1',1,1),
    ('TestUser','TestUser','2022-11-21 20:59:17','2022-11-21 20:59:17','Test comment3',1,2),
    ('TestUser','TestUser','2022-11-21 20:59:17','2022-11-21 20:59:17','Test comment4',1,3);

