create table if not exists country
(
    id          int auto_increment
        primary key,
    countryname varchar(255) null,
    countrycode varchar(255) null
);

create table if not exists sys_dict
(
    id    bigint(32) auto_increment comment '主键'
        primary key,
    code  varchar(64) not null comment '类别',
    name  varchar(64) not null comment '字典名',
    value varchar(64) not null comment '字典值'
);

create table if not exists sys_privilege
(
    id             bigint auto_increment comment '权限ID'
        primary key,
    privilege_name varchar(50)  null comment '权限名称',
    privilege_url  varchar(200) null comment '权限URL'
)
    comment '权限表';

create table if not exists sys_role
(
    id          bigint auto_increment comment '角色ID'
        primary key,
    role_name   varchar(50) null comment '角色名',
    enabled     int         null comment '有效标志',
    create_by   bigint      null comment '创建人',
    create_time datetime    null comment '创建时间'
)
    comment '角色表';

create table if not exists sys_role_privilege
(
    role_id      bigint null comment '角色ID',
    privilege_id bigint null comment '权限ID'
)
    comment '角色权限关联表';

create table if not exists sys_user
(
    id            bigint auto_increment comment '用户ID'
        primary key,
    user_name     varchar(50)                           null comment '用户名',
    user_password varchar(50)                           null comment '密码',
    user_email    varchar(50) default 'test@mybatis.tk' null comment '邮箱',
    user_info     text                                  null comment '简介',
    head_img      blob                                  null comment '头像',
    create_time   datetime                              null comment '创建时间'
)
    comment '用户表';

create table if not exists sys_user_role
(
    user_id bigint null comment '用户ID',
    role_id bigint null comment '角色ID'
)
    comment '用户角色关联表';


INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (1, '中国', 'CN');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (2, '美国', 'US');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (3, '俄罗斯', 'RU');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (4, '英国', 'GB');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (5, '法国', 'FR');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (6, '中国', 'CN');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (7, '美国', 'US');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (8, '俄罗斯', 'RU');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (9, '英国', 'GB');
INSERT INTO mybatis.country (id, countryname, countrycode) VALUES (10, '法国', 'FR');


INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (1, '性别', '男', '男');
INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (2, '性别', '女', '女');
INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (3, '季度', '第一季度', '1');
INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (4, '季度', '第二季度', '2');
INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (5, '季度', '第三季度', '3');
INSERT INTO mybatis.sys_dict (id, code, name, value) VALUES (6, '季度', '第四季度', '4');

INSERT INTO mybatis.sys_privilege (id, privilege_name, privilege_url) VALUES (1, '用户管理', '/users');
INSERT INTO mybatis.sys_privilege (id, privilege_name, privilege_url) VALUES (2, '角色管理', '/roles');
INSERT INTO mybatis.sys_privilege (id, privilege_name, privilege_url) VALUES (3, '系统日志', '/logs');
INSERT INTO mybatis.sys_privilege (id, privilege_name, privilege_url) VALUES (4, '人员维护', '/persons');
INSERT INTO mybatis.sys_privilege (id, privilege_name, privilege_url) VALUES (5, '单位维护', '/companies');

INSERT INTO mybatis.sys_role (id, role_name, enabled, create_by, create_time) VALUES (1, '管理员', 1, 1, '2016-04-01 17:02:14');
INSERT INTO mybatis.sys_role (id, role_name, enabled, create_by, create_time) VALUES (2, '普通用户', 1, 1, '2016-04-01 17:02:34');

INSERT INTO mybatis.sys_role_privilege (role_id, privilege_id) VALUES (1, 1);
INSERT INTO mybatis.sys_role_privilege (role_id, privilege_id) VALUES (1, 3);
INSERT INTO mybatis.sys_role_privilege (role_id, privilege_id) VALUES (1, 2);
INSERT INTO mybatis.sys_role_privilege (role_id, privilege_id) VALUES (2, 4);
INSERT INTO mybatis.sys_role_privilege (role_id, privilege_id) VALUES (2, 5);

INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1, 'admin', '123456', 'admin@mybatis.tk', '管理员用户', 0x1231231230, '2016-06-07 01:11:12');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1001, 'test', '123456', 'test@mybatis.tk', '测试用户', 0x1231231230, '2016-06-07 00:00:00');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1036, 'test1', '123456', 'test@mybatis.tk', 'test info', 0x010203, '2019-11-19 19:30:09');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1037, 'test1', '123456', 'test@mybatis.tk', 'test info', 0x010203, '2019-11-19 19:30:53');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1040, 'test1', '123456', 'test@mybatis.tk', 'test info', 0x010203, '2020-04-02 10:48:57');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1043, 'test1', '123456', 'test@mybatis.tk', 'test info', 0x010203, '2020-04-02 10:49:27');
INSERT INTO mybatis.sys_user (id, user_name, user_password, user_email, user_info, head_img, create_time) VALUES (1046, 'test1', '123456', 'test@mybatis.tk', 'test info', 0x010203, '2020-04-02 17:10:50');