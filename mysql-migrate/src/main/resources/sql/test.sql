# todo

drop database test;
drop database test1;
create database test;
create database test1;

create table test.t_test1
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2'
);

create table test1.t_test1
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2',
    constraint t_test1_test_uindex unique (test_field1, test_field2)
);

create table test.t_test2
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2',
    constraint t_test2_test_uindex unique (test_field1, test_field2)
);

create table test1.t_test2
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2'
);

create table test.t_test3
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2',
    constraint t_test3_test_uindex unique (test_field1, test_field2)
);

create table test1.t_test3
(
    id          int auto_increment primary key,
    test_field1 varchar(20) null comment '测试字段1',
    test_field2 varchar(20) null comment '测试字段2',
    constraint t_test3_test_uindex unique (test_field1)
);


