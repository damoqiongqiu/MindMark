/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2025/1/4 16:31:02                            */
/*==============================================================*/


drop table if exists mind_mark_db_for_process;

drop table if exists mind_mark_embedding_log;

drop table if exists mind_mark_file_upload;

drop table if exists mind_mark_rbac_user;

drop table if exists mind_mark_table_for_process;

drop table if exists mind_mark_user_index;

/*==============================================================*/
/* Table: mind_mark_db_for_process                              */
/*==============================================================*/
create table mind_mark_db_for_process
(
    id        int(11) not null auto_increment,
    db_type   varchar(64) comment '数据库类型，目前仅测试 MariaDB',
    ip        varchar(64)  not null comment '数据库的 IP 地址',
    port      int(11) not null comment '数据库的端口',
    charset   varchar(64) comment '字符集，默认 UTF-8',
    user_name varchar(64)  not null comment '数据库用户名',
    password  varchar(256) not null comment '数据库密码',
    user_id   int(11) not null comment '外键，用户 ID',
    primary key (id)
);

alter table mind_mark_db_for_process comment '需要进行处理的数据库，让用户自己配置。';

/*==============================================================*/
/* Table: mind_mark_embedding_log                               */
/*==============================================================*/
create table mind_mark_embedding_log
(
    id       int(11) not null auto_increment,
    db_id    int(11),
    table_id int(11) comment '表 ID ，记录已经处理过的表，此字段不与 file_id 共用，当 file_id 有值时，此字段应该无值',
    start_id int(11) comment '已经处理到的行 ID ，此字段与 table_id 配合使用',
    file_id  int(11) comment '记录已经处理过的文件 ID，此字段不与 table_id 共用，当 table_id 有值时，此字段应该无值',
    index_id int(11) comment '向量数据库中的索引 ID，用于记录向量化之后的数据存储在哪个索引中
            对于不同的表或者文件，用户可以指定索引',
    primary key (id)
);

alter table mind_mark_embedding_log comment '向量化记录';

/*==============================================================*/
/* Table: mind_mark_file_upload                                 */
/*==============================================================*/
create table mind_mark_file_upload
(
    id            int(11) not null auto_increment comment '主键，自增',
    file_name     varchar(255) not null comment '文件名，实际存储此文件的名称，一般由系统通过 UUID 生成，防止同一目录中文件名冲突，此名称带有后缀',
    display_name  varchar(255) not null comment '文件的展示名称，一般取文件的原始名称，不带后缀',
    file_suffix   varchar(32) comment '文件的后缀',
    file_size     bigint                default 0,
    path          varchar(255) not null comment '文件在磁盘上的完整路径
            此字段不能与 URL 字段并存',
    url           varchar(255) comment '访问此文件的路径，可以指向系统外部的 URL
            此字段不能和 path 字段并存',
    file_desc     varchar(255)          default null,
    display_order int(11) not null default 1 comment '显示顺序',
    up_time       datetime     not null default current_timestamp,
    user_id       int(11) not null default 0,
    primary key (id)
);

alter table mind_mark_file_upload comment '维护用户上传的文件。';

/*==============================================================*/
/* Table: mind_mark_rbac_user                                   */
/*==============================================================*/
create table mind_mark_rbac_user
(
    user_id     int(11) not null auto_increment,
    user_name   varchar(64) not null,
    nick_name   varchar(64) not null,
    password    varchar(64) not null default '',
    salt        varchar(32)          default '',
    email       varchar(64)          default '',
    cellphone   varchar(32)          default '',
    gender      int(11) default 0 comment '0男 1女 2未知',
    city        varchar(128),
    education   varchar(128),
    avatar_url  varchar(64)          default '' comment '用户头像 URL',
    create_time datetime             default current_timestamp,
    status      int(11) default 1 comment '-1 特权用户不能删除 0正常 1禁用 2删除',
    remark      varchar(1024)        default '',
    primary key (user_id)
);

alter table mind_mark_rbac_user comment '对于 MindMark 来说，总是会自动创建一个默认的用户叫做 mind-mark ，密码也是 mind-mark';

insert into mind_mark_rbac_user
(user_name,
 nick_name,
 password,
 salt,
 email,
 cellphone,
 gender,
 city,
 education,
 avatar_url,
 create_time,
 status,
 remark)
values ('mind-mark', -- 用户名
        'mind-mark', -- 昵称
        'mind-mark', -- 密码（明文密码）
        '', -- salt（默认为空）
        '', -- email（默认为空）
        '', -- cellphone（默认为空）
        2, -- 性别（默认为未知，值为 2）
        '', -- city（默认为空）
        '', -- education（默认为空）
        '', -- avatar_url（默认为空）
        current_timestamp, -- 创建时间（使用当前时间戳）
        0, -- 状态（默认为正常，值为 0）
        '默认用户 mind-mark' -- 备注（可以为该用户提供说明）
       );

/*==============================================================*/
/* Table: mind_mark_table_for_process                           */
/*==============================================================*/
create table mind_mark_table_for_process
(
    id          int(11) not null auto_increment,
    db_id       int(11) not null,
    schema_name varchar(128) not null comment '需要被处理的数据库名称',
    table_name  varchar(128) not null comment '需要被处理的表名称',
    id_column   varchar(128) not null comment '主键字段的名称，假设需要被处理的表带有整数自增型的 id',
    primary key (id)
);

alter table mind_mark_table_for_process comment '维护需要进行向量化的表，用户可以在 UI 界面上进行配置。';

/*==============================================================*/
/* Table: mind_mark_user_index                                  */
/*==============================================================*/
create table mind_mark_user_index
(
    id         int(11) not null auto_increment,
    user_id    int(11) not null,
    index_name varchar(256) not null comment '索引名称',
    remark     varchar(1024),
    primary key (id)
);

alter table mind_mark_user_index comment '维护用户在 ElasticSearch 中创建的索引
对于 MindMark 来说，总是会自动创建一个默认的';

insert into mind_mark_user_index(id, user_id, index_name, remark)
values (1, (select user_id from mind_mark_rbac_user where user_name = 'mind-mark'), 'mind-mark', 'MindMark 默认索引');




/*==============================================================*/

-- RBAC 相关表

/*Table structure for table `mindmark_rbac_api` */

DROP TABLE IF EXISTS `mindmark_rbac_api`;

CREATE TABLE `mindmark_rbac_api`
(
    `api_id`      int(11) NOT NULL AUTO_INCREMENT,
    `api_name`    varchar(64) NOT NULL,
    `url`         varchar(64)          DEFAULT NULL COMMENT 'URL 的匹配模式和 @RequestMapping 中的定义模式完全相同。',
    `permission`  varchar(64) NOT NULL DEFAULT '*' COMMENT '权限定义，按照 Apache Shiro 的权限定义规则进行定义。\r\n            为了避免重复和歧义，权限字符串必须是不同的。',
    `create_time` datetime    NOT NULL DEFAULT current_timestamp(),
    `update_time` datetime    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `remark`      varchar(500)         DEFAULT NULL,
    PRIMARY KEY (`api_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='用来定义服务端 API 接口的权限。';

/*Data for the table `mindmark_rbac_api` */

insert into `mindmark_rbac_api`(`api_id`, `api_name`, `url`, `permission`, `create_time`, `update_time`, `remark`)
values (5, '系统管理', NULL, '*', '2023-07-18 08:59:45', '2023-07-19 13:54:36',
        '系统管理权限是最高权限，拥有此权限可以对系统进行任意操作，删除此权限会导致系统管理员无法对系统进行维护。根据 Apache Shiro 的权限规则，此权限代码拥有最高优先级，将会覆盖其它所有权限代码。'),
       (6, '关注用户', '', 'user:follow', '2023-07-18 14:30:04', '2023-07-19 13:51:20',
        '拥有此权限可以关注用户，否则不可以。【这是一条测试数据，无意义】'),
       (7, '管理用户', '', 'sys:manage:user', '2023-07-19 13:48:36', '2023-07-19 13:48:36', '管理用户'),
       (8, '管理角色', '', 'sys:manage:role', '2023-07-19 13:51:50', '2023-07-19 13:51:50', '管理角色'),
       (9, '管理后端接口权限', '', 'sys:manage:api-permission', '2023-07-19 13:53:00', '2023-07-19 13:53:00',
        '拥有此权限代码的角色，可以维护后端接口权限。'),
       (10, '管理前端页面权限', '', 'sys:manage:component-permission', '2023-07-19 13:53:43', '2023-07-19 13:53:43',
        '拥有此权限代码的角色可以管理前端页面权限。'),
       (12, '测试数据-没有用', '', '*', '2023-07-22 16:55:50', '2023-07-26 16:38:11',
        '【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】'),
       (14, '测试111', '', '*', '2023-08-13 13:15:27', '2023-08-13 13:15:37',
        '测试接口用，请勿配置此项目。测试接口用，请勿配置此项目。测试接口用，请勿配置此项目。');


/*Table structure for table `mindmark_rbac_component` */

DROP TABLE IF EXISTS `mindmark_rbac_component`;

CREATE TABLE `mindmark_rbac_component`
(
    `component_id`   int(11) NOT NULL AUTO_INCREMENT,
    `p_id`           int(11) DEFAULT NULL COMMENT '用来构建 Tree 形菜单，P_ID=-1 表示顶级菜单',
    `component_name` varchar(64) NOT NULL,
    `icon`           varchar(64)          DEFAULT NULL,
    `url`            varchar(64)          DEFAULT NULL COMMENT '组件对应的 URL 路径，可以定义成系统外部的链接 URL',
    `display_order`  int(11) NOT NULL DEFAULT 1 COMMENT '组件在前端屏幕上的显示顺序，按数值从小到达排列，数值越小越靠屏幕顶部。\r\n            在构建树形菜单时，可以利用此列控制显示的顺序。',
    `permission`     varchar(64) NOT NULL DEFAULT '*' COMMENT '权限定义，按照 Apache Shiro 的权限定义规则进行定义。\r\n            为了避免重复和歧义，权限字符串必须是不同的。',
    `create_time`    datetime    NOT NULL DEFAULT current_timestamp(),
    `update_time`    datetime    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `visiable`       int(11) NOT NULL DEFAULT 1 COMMENT '菜单是否可见，1 可见，0 不可见',
    `remark`         varchar(500)         DEFAULT NULL,
    PRIMARY KEY (`component_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COMMENT='用来定义前端页面上的组件权限，\r\ncomponent 可以是菜单、按钮，甚至可以细致到一个 HTML 元素。\r\n';

/*Data for the table `mindmark_rbac_component` */

insert into `mindmark_rbac_component`(`component_id`, `p_id`, `component_name`, `icon`, `url`, `display_order`,
                                      `permission`, `create_time`, `update_time`, `visiable`, `remark`)
values (42, NULL, '用户管理', NULL, 'user-table/page/1', 1, 'menu:view:user-management', '2023-07-19 14:13:59',
        '2023-07-19 15:10:57', 1,
        '拥有此权限代码的角色可以看到管理后台右侧边栏的【用户管理】菜单项。\n删除此项将会导致菜单入口消失。'),
       (43, NULL, '角色管理', NULL, 'role-table/page/1', 2, 'menu:view:role-management', '2023-07-19 14:14:18',
        '2023-07-19 14:31:58', 1, '拥有此权限代码的角色可以看到管理后台右侧边栏的【角色管理】菜单项。'),
       (44, NULL, '后端接口权限', NULL, 'api-permission-table/page/1', 3, 'menu:view:api-permission-management',
        '2023-07-19 14:14:54', '2023-07-19 14:32:12', 1,
        '拥有此权限代码的角色可以看到管理后台右侧边栏的【后端接口权限】菜单项。'),
       (45, NULL, '前端页面权限', NULL, 'component-permission-table/page/1', 4,
        'menu:view:component-permission-management', '2023-07-19 14:15:18', '2023-07-19 14:32:26', 1,
        '拥有此权限代码的角色可以看到管理后台右侧边栏的【前端页面权限】菜单项。'),
       (46, NULL, '系统设置', NULL, 'sys-settings', 5, 'menu:view:sys-settings', '2023-07-19 14:15:48',
        '2023-07-19 14:32:38', 1, '拥有此权限代码的角色可以看到管理后台右侧边栏的【系统设置】菜单项。'),
       (49, NULL, '测试数据-没有用', NULL, '/test/test/test/test', 100, '*', '2023-07-22 16:56:02',
        '2023-07-26 17:57:59', 1,
        '【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】【测试数据-没有用】'),
       (54, 49, '测试子节点和展开状态', '', '', 1002, '*', '2023-08-18 14:51:48', '2023-08-18 15:29:20', 0,
        '测试数据，请不要配置。');


/*Table structure for table `mindmark_rbac_session` */

DROP TABLE IF EXISTS `mindmark_rbac_session`;

CREATE TABLE `mindmark_rbac_session`
(
    `session_id`            varchar(64) NOT NULL DEFAULT '',
    `app_name`              varchar(64)          DEFAULT NULL COMMENT '应用名称',
    `user_id`               int(11) DEFAULT NULL COMMENT '如果用户没有登录，此列可为空',
    `user_name`             varchar(64)          DEFAULT NULL,
    `creation_time`         datetime             DEFAULT NULL,
    `expiry_time`           datetime             DEFAULT NULL,
    `last_access_time`      datetime             DEFAULT NULL,
    `max_inactive_interval` int(11) DEFAULT NULL,
    `timeout`               bigint(20) DEFAULT NULL COMMENT '过期时间',
    `expired`               tinyint(1) DEFAULT 0 COMMENT 'Session 是否已经过期',
    `host`                  varchar(64)          DEFAULT '' COMMENT 'IP地址',
    `os`                    varchar(64)          DEFAULT '',
    `browser`               varchar(64)          DEFAULT '',
    `user_agent`            varchar(255)         DEFAULT NULL COMMENT '浏览器发送过来的 UserAgent 字符串',
    `session_data`          text                 DEFAULT NULL COMMENT 'Session 中的所有 Attribute ，格式是 JSON 。',
    PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用来持久化 Session ，应用端可以利用此表实现 SSO 。\r\n此表中的 SESSION_DATA 是 J';

/*Data for the table `mindmark_rbac_session` */

insert into `mindmark_rbac_session`(`session_id`, `app_name`, `user_id`, `user_name`, `creation_time`, `expiry_time`,
                                    `last_access_time`, `max_inactive_interval`, `timeout`, `expired`, `host`, `os`,
                                    `browser`, `user_agent`, `session_data`)
values ('2821dac2-ef06-4a73-af39-d5aa4e9c3dab', NULL, NULL, NULL, '2023-08-20 15:27:30', NULL, '2023-08-20 15:50:47',
        NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('2f002575-00c8-4e35-8071-d8a3a4f72a15', NULL, NULL, NULL, '2023-08-14 13:36:13', NULL, '2023-08-14 14:18:24',
        NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('3d0c3c21-a70b-4dcc-a29c-ec21aaf0a479', NULL, 2, 'admin@126.com', '2023-08-23 19:41:48', NULL,
        '2023-08-23 19:43:02', NULL, 259200000, 0, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('40cf9d7b-e605-4b3e-b94d-c5a3dd710f18', NULL, 2, 'admin@126.com', '2023-08-18 20:59:36', NULL,
        '2023-08-19 14:53:53', NULL, 259200000, 0, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('5d9d9697-4701-41ed-8803-cd0dffc84663', NULL, 2, 'admin@126.com', '2023-08-13 21:54:36', NULL,
        '2023-08-13 21:59:23', NULL, 259200000, 0, '0:0:0:0:0:0:0:1', 'Windows 10', 'Chrome 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36',
        NULL),
       ('78a6bc73-464f-440d-9b07-dc5676e34489', NULL, NULL, NULL, '2023-08-14 14:37:19', NULL, '2023-08-14 15:08:07',
        NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('9c3ab919-ff8a-4eac-b060-ce3a4b3d99bc', NULL, NULL, NULL, '2023-08-18 13:03:00', NULL, '2023-08-18 20:59:36',
        NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('af1216eb-6c9a-4c57-bfbf-85a6f97840b2', NULL, 2, 'admin@126.com', '2023-08-20 15:50:50', NULL,
        '2023-08-21 12:11:34', NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('bd29d2d2-74e0-46c5-9cad-ecc1cdc84a8c', NULL, NULL, NULL, '2023-08-13 21:54:36', NULL, '2023-08-13 21:54:36',
        NULL, 259200000, 0, '0:0:0:0:0:0:0:1', 'Windows 10', 'Chrome 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36',
        NULL),
       ('d5872822-8eec-43f8-a723-ae5f6e3a4e6d', NULL, NULL, NULL, '2023-08-13 14:25:10', NULL, '2023-08-14 13:36:12',
        NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('ec621aa3-fdf1-4b0c-a4a7-89173500def1', NULL, 4, 'user1@126.com', '2023-08-21 12:11:53', NULL,
        '2023-08-21 12:13:30', NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('f220742d-14b1-4adb-9679-88d1f19cb9cb', NULL, 2, 'admin@126.com', '2023-08-14 14:18:26', NULL,
        '2023-08-14 14:37:18', NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('fcf2c2c3-8686-472e-809f-6bfe16795c5d', NULL, 2, 'admin@126.com', '2023-08-21 12:14:14', NULL,
        '2023-08-23 19:39:26', NULL, 259200000, 1, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL),
       ('fe1cb959-7924-41c6-b1fb-5cb3a9e70b21', NULL, NULL, NULL, '2023-08-23 19:41:48', NULL, '2023-08-23 19:41:48',
        NULL, 259200000, 0, '0:0:0:0:0:0:0:1', 'Windows 10', 'Firefox 11',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0', NULL);

/*Table structure for table `mindmark_rbac_role_api` */

DROP TABLE IF EXISTS `mindmark_rbac_role_api`;

CREATE TABLE `mindmark_rbac_role_api`
(
    `role_id` int(11) NOT NULL,
    `api_id`  int(11) NOT NULL,
    PRIMARY KEY (`role_id`, `api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与 API  接口之间的关联关系。';

/*Data for the table `mindmark_rbac_role_api` */

insert into `mindmark_rbac_role_api`(`role_id`, `api_id`)
values (1, 5),
       (10, 7),
       (12, 12),
       (14, 5);


/*Table structure for table `mindmark_rbac_role_component` */

DROP TABLE IF EXISTS `mindmark_rbac_role_component`;

CREATE TABLE `mindmark_rbac_role_component`
(
    `role_id`      int(11) NOT NULL,
    `component_id` int(11) NOT NULL,
    PRIMARY KEY (`role_id`, `component_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单的关联关系';

/*Data for the table `mindmark_rbac_role_component` */

insert into `mindmark_rbac_role_component`(`role_id`, `component_id`)
values (1, 42),
       (1, 43),
       (1, 44),
       (1, 45),
       (1, 46),
       (10, 42);


/*Table structure for table `mindmark_rbac_role` */

DROP TABLE IF EXISTS `mindmark_rbac_role`;

CREATE TABLE `mindmark_rbac_role`
(
    `role_id`   int(11) NOT NULL AUTO_INCREMENT,
    `role_name` varchar(64) NOT NULL,
    `status`    int(11) NOT NULL DEFAULT 0 COMMENT '-1 特权角色，不能删除 0正常 1停用 2删除',
    `remark`    varchar(500) DEFAULT '',
    PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

/*Data for the table `mindmark_rbac_role` */

insert into `mindmark_rbac_role`(`role_id`, `role_name`, `status`, `remark`)
values (1, '系统管理员', 1, '【系统管理员角色拥有系统最高权限，删除或者禁用此角色将会导致管理员无法登录系统。】'),
       (10, '测试角色-1', 1, '此角色用来测试，上面没有配置权限点。'),
       (12, '普通用户默认角色11111', 1,
        '普通用户拥有的默认角色，删除此角色将导致普通用户失去对应的接口和菜单权限。【普通用户默认角色11111】'),
       (13, '测试角色2222', 1, '测试角色2222测试角色2222测试角色2222测试角色2222'),
       (14, '测试角色3333', 1,
        '测试角色3333测试角色3333测试角色3333测试角色3333测试角色3333测试角色3333测试角色3333测试角色3333');


/*Table structure for table `mindmark_rbac_user` */

DROP TABLE IF EXISTS `mindmark_rbac_user`;

CREATE TABLE `mindmark_rbac_user`
(
    `user_id`     int(11) NOT NULL AUTO_INCREMENT,
    `user_name`   varchar(64) NOT NULL,
    `nick_name`   varchar(64) NOT NULL,
    `password`    varchar(64)  DEFAULT '',
    `email`       varchar(64)  DEFAULT '',
    `cellphone`   varchar(32)  DEFAULT '',
    `gender`      int(11) DEFAULT 0 COMMENT '0男 1女 2未知',
    `city`        varchar(128) DEFAULT NULL,
    `education`   varchar(128) DEFAULT NULL,
    `avatar_url`  varchar(64)  DEFAULT '' COMMENT '用户头像 URL',
    `salt`        varchar(32)  DEFAULT '',
    `create_time` datetime     DEFAULT current_timestamp(),
    `status`      int(11) DEFAULT 1 COMMENT '-1 特权用户不能删除 0正常 1禁用 2删除',
    `remark`      varchar(500) DEFAULT '',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

/*Data for the table `mindmark_rbac_user` */

insert into `mindmark_rbac_user`(`user_id`, `user_name`, `nick_name`, `password`, `email`, `cellphone`, `gender`,
                                 `city`, `education`, `avatar_url`, `salt`, `create_time`, `status`, `remark`)
values (2, 'admin@126.com', 'admin', '7f9091af32f1f34b430998540d9b8cfc', 'admin@126.com', '13813838338', 2, NULL, NULL,
        '', '850bf8', '2023-07-17 23:02:37', 1,
        'adminadminadminadminadminadminadminadminadminadminadminadminadminadminadmin'),
       (4, 'user1@126.com', '测试用户-1', '5f077f98795ddfe4d353e4c2a0acbdd0', 'user1@126.com', '', 1, NULL, NULL, '',
        '596a69', '2023-07-18 13:58:04', 1, ''),
       (5, 'user2@126.com', 'user2', '4f9c512e8ca73e1285a5617fb843e609', 'user2@126.com', '', 0, NULL, NULL, '',
        '558d9a', '2023-07-18 15:10:22', 1, ''),
       (6, 'user3@126.com', 'user3', 'ae2a2f18656334fab7d3ea71c6bc55ce', 'user3@126.com', '', 0, NULL, NULL, '',
        'ec764e', '2023-07-18 16:20:58', 1, ''),
       (7, 'user4@126.com', 'user4', '9ee2e0077263167a2c1f62fa7f993da1', 'user4@126.com', '', 2, NULL, NULL, '',
        '3cb61c', '2023-07-18 20:24:12', 1, ''),
       (10, 'user5@126.com', 'user5', '2513aac029d2863024ad16133ced58c1', '', '', 1, NULL, NULL, '', '7ce10d',
        '2023-07-26 15:18:53', 0, 'user5'),
       (11, 'user6@126.com', 'user6', '773d107cf9d07154d954a9630bcc9002', 'user6@126.com', '', 0, NULL, NULL, '',
        '29322b', '2023-07-27 13:14:13', 1,
        '【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】【user6】'),
       (12, 'test22@126.com', '', 'e0f543b23b37a0c9ace53eed201f915f', 'test22@126.com', '', 0, NULL, NULL, '', '483c0e',
        '2023-08-12 00:11:03', 1, ''),
       (13, 'test3333@126.com', 'test3333', '85e162e858e8a8493e37bf1b29907a72', 'test3333@126.com', '', 0, NULL, NULL,
        '', '1ea4a8', '2023-08-13 14:12:13', 1,
        'test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333test3333');


/*Table structure for table `mindmark_rbac_user_role` */

DROP TABLE IF EXISTS `mindmark_rbac_user_role`;

CREATE TABLE `mindmark_rbac_user_role`
(
    `user_id` int(11) NOT NULL,
    `role_id` int(11) NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `mindmark_rbac_user_role` */

insert into `mindmark_rbac_user_role`(`user_id`, `role_id`)
values (2, 1),
       (2, 10),
       (2, 12),
       (4, 10),
       (7, 10);
