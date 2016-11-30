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
  event_date                    timestamp,
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

create table event_tag (
  event_id                      bigint not null,
  tag_id                        bigint not null,
  constraint pk_event_tag primary key (event_id,tag_id)
);

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

create table tag (
  id                            bigint not null,
  name                          varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_update                   timestamp not null,
  constraint pk_tag primary key (id)
);
create sequence tag_seq;

alter table comment add constraint fk_comment_event_id foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_comment_event_id on comment (event_id);

alter table event add constraint fk_event_organiser_id foreign key (organiser_id) references organiser (id) on delete restrict on update restrict;

alter table event_tag add constraint fk_event_tag_event foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_event_tag_event on event_tag (event_id);

alter table event_tag add constraint fk_event_tag_tag foreign key (tag_id) references tag (id) on delete restrict on update restrict;
create index ix_event_tag_tag on event_tag (tag_id);


# --- !Downs

alter table comment drop constraint if exists fk_comment_event_id;
drop index if exists ix_comment_event_id;

alter table event drop constraint if exists fk_event_organiser_id;

alter table event_tag drop constraint if exists fk_event_tag_event;
drop index if exists ix_event_tag_event;

alter table event_tag drop constraint if exists fk_event_tag_tag;
drop index if exists ix_event_tag_tag;

drop table if exists comment;
drop sequence if exists comment_seq;

drop table if exists event;
drop sequence if exists event_seq;

drop table if exists event_tag;

drop table if exists organiser;
drop sequence if exists organiser_seq;

drop table if exists tag;
drop sequence if exists tag_seq;

