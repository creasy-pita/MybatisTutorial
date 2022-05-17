
CREATE TABLE  student(
     ID int NOT NULL ,
     NAME varchar(100) NOT NULL,
     icon blob
);


alter table student add create_time date;
-- 情形1
alter table student add enabled Number(1,0);
-- 情形2
alter table student add enabled varchar2(5);
