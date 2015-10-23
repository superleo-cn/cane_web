# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table gz_crane (
  id                        bigint auto_increment not null,
  device_id                 varchar(255),
  imsi                      varchar(255),
  iccid                     varchar(255),
  sos_one                   varchar(255),
  sos_two                   varchar(255),
  gps_switch                integer,
  has_new_data              integer,
  created                   datetime(6),
  constraint pk_gz_crane primary key (id))
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
  created                   datetime(6),
  constraint pk_gps_collection primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table gz_crane;

drop table gps_collection;

SET FOREIGN_KEY_CHECKS=1;

