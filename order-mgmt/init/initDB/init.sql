alter table if exists "order_line" drop constraint if exists "FKmlw5sqej7k7y9paxcppxv0hhg";
drop table if exists "order" cascade;
drop table if exists "order_line" cascade;
drop table if exists "outbox" cascade;
drop table if exists "product" cascade;
drop sequence if exists "order_seq";
drop sequence if exists "order_line_seq";
create sequence "order_seq" start with 1 increment by 50;
create sequence "order_line_seq" start with 1 increment by 50;
create table "order" ("customerid" integer, "delivery_day" date, "status" smallint check ("status" between 0 and 2), "total_price" float(53), "id" bigint not null, "version" bigint not null, primary key ("id"));
create table "order_line" ("quantity" integer, "unit_price_at_shopping" float(53), "id" bigint not null, "order_id" bigint, "productid" bigint, primary key ("id"));
create table "outbox" ("created_at" timestamp(6), "event_id" uuid not null, "aggregate_id" varchar(255), "aggregate_type" varchar(255), "custom_headers" varchar(255), "event_type" varchar(255), "payload" "jsonb", primary key ("event_id"));
create table "product" ("active" boolean not null, "unit_price" float(53) not null, "id" bigint not null, "version" bigint not null, "name" varchar(20) not null, primary key ("id"));
alter table if exists "order_line" add constraint "FKmlw5sqej7k7y9paxcppxv0hhg" foreign key ("order_id") references "order";

