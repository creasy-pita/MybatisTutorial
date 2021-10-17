create database mybatis-study;
use mybatis-study;
create table `user`(
    id int primary key,
    name varchar(10),
    pwd varchar(20),
    user_age int
);

create table `teacher`(
    id int primary key,
    name varchar(20) default  null
) engine=innodb default charset=utf8;

insert into teacher(id, name) values (1, '秦老师');

create table `student`(
    id int primary key,
    name varchar(10) default null,
    tid int,
    key  'fktid' (tid),
    constraint 'fktid' FOREIGN key (tid) references  `teacher` (id)
) engine=innodb default  charset=utf8;

insert into student (id,name,tid) values (1, '小明',1);
insert into student (id,name,tid) values (2, '小xia',1);
insert into student (id,name,tid) values (3, '小song',1);
insert into student (id,name,tid) values (4, '小li',1);
insert into student (id,name,tid) values (5, '小jun',1);