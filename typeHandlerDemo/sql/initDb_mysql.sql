create database mybatis-study;
use mybatis-study;

CREATE TABLE  student(
     ID int(10) NOT NULL ,
     NAME varchar(100) NOT NULL,
     icon longblob
);


insert into student (id,name,icon) values (1, '小明','a');
insert into student (id,name,icon) values (2, '小xia','b');
insert into student (id,name,icon) values (3, '小song','c');
insert into student (id,name,icon) values (4, '小li','d');
insert into student (id,name,icon) values (5, '小jun','e');