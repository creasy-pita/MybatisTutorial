
CREATE TABLE  student(
     ID int NOT NULL ,
     NAME varchar(100) NOT NULL,
     icon bytea
);

alter table student add create_time timestamp;
-- alter table student add enabled bool;
alter table student add enabled numeric(1,0);
