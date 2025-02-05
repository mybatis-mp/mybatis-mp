/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

drop table if exists t_sys_user;

CREATE TABLE t_sys_user
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name CHAR(100) default '123456',
    password CHAR(100),
    role_id INTEGER,
    create_time DATETIME NOT NULL DEFAULT current_timestamp
);

drop table if exists sys_role;

CREATE TABLE IF NOT EXISTS sys_role
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name CHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT current_timestamp
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
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    create_time DATETIME NOT NULL DEFAULT current_timestamp
);

drop table if exists uuid_test;

CREATE TABLE IF NOT EXISTS uuid_test
(
    id varchar(64),
    create_time DATETIME NOT NULL DEFAULT current_timestamp
);

drop table if exists version_test;

CREATE TABLE IF NOT EXISTS version_test
(
    id varchar(32) PRIMARY KEY,
    version INTEGER NOT NULL,
    name CHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT current_timestamp
);

drop table if exists tenant_test;

CREATE TABLE IF NOT EXISTS tenant_test
(
    id varchar(32) PRIMARY KEY,
    tenant_id INTEGER NOT NULL,
    name CHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT current_timestamp
);

drop table if exists logic_delete_test;

CREATE TABLE IF NOT EXISTS logic_delete_test
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name CHAR(100) not null,
    deleted TINYINTEGER not NULL default 0,
    delete_time DATETIME
);

insert into logic_delete_test
values (1, '测试', 0, null),
       (2, '运维', 0, null),
       (3, '运维2', 0, null);

drop table if exists default_value_test;

CREATE TABLE IF NOT EXISTS default_value_test
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    value1 CHAR(100) not null,
    value2 INTEGER not NULL ,
    value3 CHAR(100),
    value4 INTEGER ,
    create_time DATETIME NOT NULL
);

drop table if exists composite_test;

CREATE TABLE IF NOT EXISTS composite_test
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    version int not null,
    tenant_id int not null,
    deleted TINYINTEGER not NULL default 0,
    delete_time DATETIME
);

drop table if exists nested_first;

CREATE TABLE IF NOT EXISTS nested_first
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    th_name CHAR(100) not null
);

drop table if exists nested_second;

CREATE TABLE IF NOT EXISTS nested_second
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nested_one_id INTEGER NOT NULL,
    th_name CHAR(100) not null
);

drop table if exists nested_third;

CREATE TABLE IF NOT EXISTS nested_third
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nested_second_id INTEGER NOT NULL,
    th_name CHAR(100) not null
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
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    th_name CHAR(100) not null
);

drop table if exists nested_muti_second;

CREATE TABLE IF NOT EXISTS nested_muti_second
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nested_one_id INTEGER NOT NULL,
    th_name CHAR(100) not null
);

drop table if exists nested_muti_third;

CREATE TABLE IF NOT EXISTS nested_muti_third
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nested_second_id INTEGER NOT NULL,
    th_name CHAR(100) not null
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


drop table if exists multi_pk;

CREATE TABLE IF NOT EXISTS multi_pk
(
    id1 INTEGER NOT NULL,
    id2 INTEGER NOT NULL,
    name CHAR(100) not null,
    PRIMARY KEY(id1,id2)
);