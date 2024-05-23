drop table if exists t_sys_user;

CREATE TABLE IF NOT EXISTS t_sys_user
(
    id INTEGER PRIMARY KEY auto_increment,
    user_name VARCHAR(100),
    password VARCHAR(100),
    role_id INTEGER,
    create_time DATETIME NOT NULL DEFAULT NOW()
);

drop table if exists sys_role;

CREATE TABLE IF NOT EXISTS sys_role
(
    id INTEGER PRIMARY KEY auto_increment,
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW()
);

drop table if exists sys_user_score;

CREATE TABLE IF NOT EXISTS sys_user_score
(
    user_id INTEGER PRIMARY KEY,
    score decimal(6, 2)
);

insert into t_sys_user
values (1, 'admin', '123', 0, '2023-10-11 15:16:17'),
       (2, 'test1', '123456', 1, '2023-10-11 15:16:17'),
       (3, 'test2', null, 1, '2023-10-12 15:16:17');

insert into sys_role
values (1, '测试', '2022-10-10'),
       (2, '运维', '2022-10-10');


insert into sys_user_score
values (2, 3.2),
       (3, 2.6);

drop table if exists id_test;

CREATE TABLE IF NOT EXISTS id_test
(
    id BIGINT PRIMARY KEY auto_increment,
    create_time DATETIME NOT NULL DEFAULT NOW()
);

drop table if exists version_test;

CREATE TABLE IF NOT EXISTS version_test
(
    id varchar(32) PRIMARY KEY,
    version INT NOT NULL,
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW()
);

drop table if exists tenant_test;

CREATE TABLE IF NOT EXISTS tenant_test
(
    id varchar(32) PRIMARY KEY,
    tenant_id INT NOT NULL,
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW()
);

drop table if exists logic_delete_test;

CREATE TABLE IF NOT EXISTS logic_delete_test
(
    id BIGINT PRIMARY KEY auto_increment,
    name VARCHAR(100) not null,
    deleted TINYINT not NULL default 0,
    delete_time DATETIME
);

insert into logic_delete_test
values (1, '测试', 0, null),
       (2, '运维', 0, null),
       (3, '运维2', 0, null);

drop table if exists default_value_test;

CREATE TABLE IF NOT EXISTS default_value_test
(
    id INT PRIMARY KEY auto_increment,
    value1 VARCHAR(100) not null,
    value2 INT not NULL ,
    value3 VARCHAR(100),
    create_time DATETIME NOT NULL
);

drop table if exists composite_test;

CREATE TABLE IF NOT EXISTS composite_test
(
    id BIGINT PRIMARY KEY auto_increment,
    version int not null,
    tenant_id int not null,
    deleted TINYINT not NULL default 0,
    delete_time DATETIME
);

drop table if exists nested_first;

CREATE TABLE IF NOT EXISTS nested_first
(
    id INT PRIMARY KEY auto_increment,
    th_name VARCHAR(100) not null
);

drop table if exists nested_second;

CREATE TABLE IF NOT EXISTS nested_second
(
    id INT PRIMARY KEY auto_increment,
    nested_one_id INT NOT NULL,
    th_name VARCHAR(100) not null
);

drop table if exists nested_third;

CREATE TABLE IF NOT EXISTS nested_third
(
    id INT PRIMARY KEY auto_increment,
    nested_second_id INT NOT NULL,
    th_name VARCHAR(100) not null
);

insert into nested_first(id,th_name)
values
    (1,'嵌套A'),
    (2,'嵌套B');

insert into nested_second(id,nested_one_id,th_name)
values
    (1,1,'嵌套AA'),
    (2,2,'嵌套BA');

insert into nested_third(id,nested_second_id,th_name)
values
    (1,1,'嵌套AAA'),
    (2,2,'嵌套BAA');



drop table if exists nested_muti_first;

CREATE TABLE IF NOT EXISTS nested_muti_first
(
    id INT PRIMARY KEY auto_increment,
    th_name VARCHAR(100) not null
);

drop table if exists nested_muti_second;

CREATE TABLE IF NOT EXISTS nested_muti_second
(
    id INT PRIMARY KEY auto_increment,
    nested_one_id INT NOT NULL,
    th_name VARCHAR(100) not null
);

drop table if exists nested_muti_third;

CREATE TABLE IF NOT EXISTS nested_muti_third
(
    id INT PRIMARY KEY auto_increment,
    nested_second_id INT NOT NULL,
    th_name VARCHAR(100) not null
);

insert into nested_muti_first(id,th_name)
values
    (1,'嵌套A'),
    (2,'嵌套B');

insert into nested_muti_second(id,nested_one_id,th_name)
values
    (1,1,'嵌套AA'),
    (2,1,'嵌套AB'),
    (3,2,'嵌套BA');

insert into nested_muti_third(id,nested_second_id,th_name)
values
    (1,2,'嵌套BAA'),
    (2,2,'嵌套BAB');