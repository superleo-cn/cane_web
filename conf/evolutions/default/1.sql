# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table gz_crane (
  id                        bigint auto_increment not null,
  device_id                 varchar(255),
  name                      varchar(255),
  type                      varchar(255),
  pass_code                 varchar(255),
  status                    tinyint(1) default 0,
  create_by                 varchar(255),
  modified_by               varchar(255),
  create_date               datetime(6),
  modified_date             datetime(6),
  user_id                   bigint,
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

