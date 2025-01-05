drop table if exists "outbox" cascade;
drop table if exists "product" cascade;
drop sequence if exists "product_seq";
create sequence "product_seq" start with 1 increment by 50;
create table "outbox" ("created_at" timestamp(6), "event_id" uuid not null, "aggregate_id" varchar(255), "aggregate_type" varchar(255), "custom_headers" varchar(255), "event_type" varchar(255), "payload" "jsonb", primary key ("event_id"));
create table "product" ("active" boolean not null, "unit_price" float(53), "id" bigint not null, "version" bigint not null, "name" varchar(20), primary key ("id"));

