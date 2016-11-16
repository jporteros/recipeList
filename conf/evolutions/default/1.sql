# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event (
  id                            bigint not null,
  name                          varchar(255),
  description                   varchar(255),
  type                          varchar(255),
  author                        varchar(255),
  constraint pk_event primary key (id)
);
create sequence event_seq;


# --- !Downs

drop table if exists event;
drop sequence if exists event_seq;

