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

CREATE TABLE IF NOT EXISTS t_sys_user
(
    id INTEGER  generated   always   as   identity   (start   with   1,increment   by   1),
    user_name VARCHAR(100) DEFAULT '123456',
    password VARCHAR(100),
    role_id INTEGER,
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

drop table if exists sys_role;

CREATE TABLE IF NOT EXISTS sys_role
(
    id INTEGER  generated   always   as   identity   (start   with   1,increment   by   1),
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

drop table if exists sys_user_score;

CREATE TABLE IF NOT EXISTS sys_user_score
(
    user_id INTEGER not null,
    score decimal(6, 2),
    PRIMARY KEY(user_id)
);


insert into t_sys_user(user_name, password, role_id, create_time)
values ( 'admin', '123', 0, '2023-10-11 15:16:17'),
       ( 'test1', '123456', 1, '2023-10-11 15:16:17'),
       ( 'test2', null, 1, '2023-10-12 15:16:17');

insert into sys_role( name, create_time)
values ('测试', '2022-10-10'),
       ('运维', '2022-10-10');


insert into sys_user_score(user_id,score)
values (2, 3.2),
       (3, 2.6);

drop table if exists id_test;

CREATE TABLE IF NOT EXISTS id_test
(
    id BIGINT  generated   always   as   identity   (start   with   1,increment   by   1),
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS uuid_test
(
    id varchar(64) not null,
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

drop table if exists version_test;

CREATE TABLE IF NOT EXISTS version_test
(
    id varchar(32) NOT NULL,
    version INT NOT NULL,
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

drop table if exists tenant_test;

CREATE TABLE IF NOT EXISTS tenant_test
(
    id varchar(32) NOT NULL,
    tenant_id INT NOT NULL,
    name VARCHAR(100) not null,
    create_time DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(id)
);

drop table if exists logic_delete_test;

CREATE TABLE IF NOT EXISTS logic_delete_test
(
    id BIGINT  generated   always   as   identity   (start   with   1,increment   by   1),
    name VARCHAR(100) not null,
    deleted SMALLINT not NULL default 0,
    delete_time DATETIME,
    PRIMARY KEY(id)
);

insert into logic_delete_test(name, deleted, delete_time)
values ( '测试', 0, null),
       ( '运维', 0, null),
       ( '运维2', 0, null);

drop table if exists default_value_test;

CREATE TABLE IF NOT EXISTS default_value_test
(
    id INT  generated   always   as   identity   (start   with   1,increment   by   1),
    value1 VARCHAR(100) not null,
    value2 INT not NULL ,
    value3 VARCHAR(100),
    value4 INT ,
    create_time DATETIME NOT NULL,
    PRIMARY KEY(id)
);

drop table if exists composite_test;

CREATE TABLE IF NOT EXISTS composite_test
(
    id BIGINT  generated   always   as   identity   (start   with   1,increment   by   1),
    version int not null,
    tenant_id int not null,
    deleted SMALLINT not NULL default 0,
    delete_time DATETIME,
    PRIMARY KEY(id)
);

drop table if exists nested_first;

CREATE TABLE IF NOT EXISTS nested_first
(
    id INT  NOT NULL ,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
);

drop table if exists nested_second;

CREATE TABLE IF NOT EXISTS nested_second
(
    id INT  NOT NULL ,
    nested_one_id INT NOT NULL,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
);

drop table if exists nested_third;

CREATE TABLE IF NOT EXISTS nested_third
(
    id INT  NOT NULL ,
    nested_second_id INT NOT NULL,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
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
    id INT  NOT NULL ,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
);

drop table if exists nested_muti_second;

CREATE TABLE IF NOT EXISTS nested_muti_second
(
    id INT  NOT NULL ,
    nested_one_id INT NOT NULL,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
);

drop table if exists nested_muti_third;

CREATE TABLE IF NOT EXISTS nested_muti_third
(
    id INT  NOT NULL ,
    nested_second_id INT NOT NULL,
    th_name VARCHAR(100) not null,
    PRIMARY KEY(id)
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
    id1 INT NOT NULL,
    id2 INT NOT NULL,
    name VARCHAR(100) not null,
    PRIMARY KEY(id1,id2)
);