# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                            bigint not null,
  author                        varchar(255),
  stars                         integer,
  body                          varchar(255),
  event_id                      bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_update                   timestamp not null,
  constraint pk_comment primary key (id)
);
create sequence comment_seq;

create table event (
  id                            bigint not null,
  name                          varchar(255),
  description                   varchar(255),
  type                          varchar(255),
  organiser_id                  bigint,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_update                   timestamp not null,
  constraint uq_event_organiser_id unique (organiser_id),
  constraint pk_event primary key (id)
);
create sequence event_seq;

create table organiser (
  id                            bigint not null,
  name                          varchar(255),
  surname                       varchar(255),
  email                         varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_update                   timestamp not null,
  constraint pk_organiser primary key (id)
);
create sequence organiser_seq;

alter table comment add constraint fk_comment_event_id foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_comment_event_id on comment (event_id);

alter table event add constraint fk_event_organiser_id foreign key (organiser_id) references organiser (id) on delete restrict on update restrict;


# --- !Downs

alter table comment drop constraint if exists fk_comment_event_id;
drop index if exists ix_comment_event_id;

alter table event drop constraint if exists fk_event_organiser_id;

drop table if exists comment;
drop sequence if exists comment_seq;

drop table if exists event;
drop sequence if exists event_seq;

drop table if exists organiser;
drop sequence if exists organiser_seq;

