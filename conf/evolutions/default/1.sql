# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table gz_cane (
  id                        bigint auto_increment not null,
  device_id                 varchar(255),
  imsi                      varchar(255),
  iccid                     varchar(255),
  sos_one                   varchar(255),
  sos_two                   varchar(255),
  gps_switch                integer,
  has_new_data              integer,
  created                   datetime(6),
  constraint pk_gz_cane primary key (id))
;

create table gz_contact (
  id                        bigint auto_increment not null,
  device_id                 varchar(255),
  name                      varchar(255),
  cell_number               varchar(255),
  status                    tinyint(1) default 0,
  created                   datetime(6),
  constraint pk_gz_contact primary key (id))
;

create table gps_collection (
  id                        bigint auto_increment not null,
  device_id                 varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  acc                       varchar(255),
  cell_id                   varchar(255),
  lac                       varchar(255),
  plmn                      varchar(255),
  orientation               integer,
  flag                      integer,
  created                   datetime(6),
  constraint pk_gps_collection primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table gz_cane;

drop table gz_contact;

drop table gps_collection;

SET FOREIGN_KEY_CHECKS=1;

