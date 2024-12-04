# 数据库初始化

-- 创建库
create database if not exists lantu_api;

-- 切换库
use lantu_api;

-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    username      varchar(256)                           null comment '用户名',
    password      varchar(512)                           null comment '密码',
    nickname      varchar(256)                           null comment '昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    gender        tinyint                                null comment '性别',
    email         varchar(256)                           null comment '邮箱',
    phone_number  varchar(32)                            null comment '手机号',
    role_id       bigint                                 null comment '角色id',
    user_points   int                                    not null comment '积分',
    access_key    varchar(512)                           null comment 'accessKey',
    secret_key    varchar(512)                           null comment 'secretKey',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除'
) comment '用户表' collate = utf8mb4_unicode_ci;
insert into `user` (`id`, `username`, `password`, `nickname`, `gender`, `role_id`, `user_points`, `access_key`, `secret_key`)
values (1, 'admin','$2a$10$mvEZKI9k.STo6lczs8CsW..yO1Kh6u5J/1EoeBLNTvTCBpJM7Htli', '管理员', 0, 1, 1000, '6d2a4fa8ada6c89b4cf65045f1713435', 'd0e1ad226b64e9e78eec8b53945bb9ad');

-- 角色表
create table if not exists role
(
    id         bigint auto_increment comment 'id' primary key,
    role_name    varchar(64)                            null comment '角色名称',
    role_desc    varchar(256)                           null comment '角色描述',
    role_type    varchar(2)                             null comment '角色类型',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint      default 0                 not null comment '是否删除'
) comment '角色表' collate = utf8mb4_unicode_ci;
insert into `role` (`id`, `role_name`, `role_desc`) values (1, '超级管理员','超级管理员');
insert into `role` (`id`, `role_name`, `role_desc`) values (2, '普通用户','普通用户');

-- 权限表
create table if not exists authority
(
    id             bigint auto_increment comment 'id' primary key,
    code           varchar(64)                            null comment '权限标识符',
    menu_name      varchar(64)                            null comment '菜单名称',
    order_no       int                                    null comment '菜单顺序',
    parent_id      bigint                                 null comment '父节点id',
    authority_type varchar(10)                            null comment '权限类型 menu/button',
    route_path     varchar(128)                           null comment '路由路径',
    hidden         tinyint                                null comment '是否隐藏路由菜单(0 - 否，1 - 是)',
    menu_icon      varchar(64)                            null comment '菜单图标',
    component      varchar(128)                           null comment '组件',
    component_name varchar(64)                            null comment '组件名称',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '权限表' collate = utf8mb4_unicode_ci;
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (1, 'interfaceList', '接口列表', 1, 0, 'menu', '/interfaceList', 0, 'icon-list', NULL, 'InterfaceList');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (2, 'accountSetting', '账号设置', 2, 0, 'menu', '/accountSetting', 0, 'icon-account-settings', '', 'AccountSetting');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (3, 'interfaceManage', '接口管理', 3, 0, 'menu', '/interfaceManage', 0, 'icon-apps', NULL, 'RouteView');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (4, 'systemManage', '系统管理', 4, 0, 'menu', '/systemManage', 0, 'icon-setting', NULL, 'RouteView');

insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (5, 'interfaceManage:index', '接口管理', 1, 3, 'menu', '/interfaceManage/index', 0, NULL, NULL, 'InterfaceManage');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (6, 'interfaceManage:analysis', '接口分析', 2, 3, 'menu', '/interfaceManage/analysis', 0, NULL, NULL, 'InterfaceAnalysis');

insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (7, 'systemManage:userManage', '用户管理', 1, 4, 'menu', '/systemManage/userManage', 0, NULL, NULL, 'UserManage');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (8, 'systemManage:roleManage', '角色管理', 2, 4, 'menu', '/systemManage/roleManage', 0, NULL, NULL, 'RoleManage');
insert into `authority` (`id`, `code`, `menu_name`, `order_no`, `parent_id`, `authority_type`, `route_path`, `hidden`, `menu_icon`, `component`, `component_name`)
values (9, 'systemManage:authorityManage', '权限管理', 3, 4, 'menu', '/systemManage/authorityManage', 0, NULL, NULL, 'AuthorityManage');

-- 角色权限表
create table if not exists role_authority
(
    id             bigint auto_increment comment 'id' primary key,
    authority_id   bigint                                 null comment '权限id',
    role_id        bigint                                 null comment '角色id',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '角色权限表' collate = utf8mb4_unicode_ci;
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (1, 1, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (2, 2, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (3, 3, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (4, 4, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (5, 5, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (6, 6, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (7, 7, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (8, 8, 1);
insert into `role_authority` (`id`, `authority_id`, `role_id`) values (9, 9, 1);

-- 接口信息
create table if not exists `interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `interface_avatar` varchar(1024) null comment '接口头像',
    `url` varchar(512) not null comment '接口地址',
    `request_params` text null comment '请求参数',
    `request_header` text null comment '请求头',
    `response_header` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `user_id` bigint not null comment '创建人',
    `consume_points` int not null comment '消耗积分',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete` tinyint default 0 not null comment '是否删除'
) comment '接口信息' collate = utf8mb4_unicode_ci;
insert into `interface_info` (`name`, `description`, `interface_avatar`, `url`, `request_params`, `request_header`, `response_header`, `status`, `method`, `user_id`, `consume_points`)
values ('查询IP信息', '查询当前IP信息','', 'http://localhost:8088/api/ipInfo', '[]', '[]', '[]', 0, 'GET', 1, 1);
insert into `interface_info` (`name`, `description`, `interface_avatar`, `url`, `request_params`, `request_header`, `response_header`, `status`, `method`, `user_id`, `consume_points`)
values ('输出hello world', '程序员的第一段代码','', 'http://localhost:8088/api/helloworld', '[{\"fieldName\":\"text\",\"type\":\"string\",\"description\":\"\"}]', '[]', '[]', 0, 'GET', 1, 1);

-- 用户调用接口关系表
create table if not exists `user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `user_id` bigint not null comment '调用用户 id',
    `interface_info_id` bigint not null comment '接口 id',
    `total_num` int default 0 not null comment '总调用次数',
    `left_num` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete` tinyint default 0 not null comment '是否删除'
) comment '用户调用接口关系' collate = utf8mb4_unicode_ci;

