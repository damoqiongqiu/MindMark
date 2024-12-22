/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2024/12/22 19:45:45                          */
/*==============================================================*/


drop table if exists mind_mark_db_for_process;

drop table if exists mind_mark_embedding_log;

drop table if exists mind_mark_file_upload;

drop table if exists mind_mark_rbac_user;

drop table if exists mind_mark_schema_for_process;

drop table if exists mind_mark_table_for_process;

drop table if exists mind_mark_user_index;

/*==============================================================*/
/* Table: mind_mark_db_for_process                              */
/*==============================================================*/
create table mind_mark_db_for_process
(
   id                   int(11) not null auto_increment,
   db_type              varchar(64) comment '数据库类型，目前仅测试 MariaDB',
   ip                   varchar(64) not null comment '数据库的 IP 地址',
   port                 int(11) not null comment '数据库的端口',
   charset              varchar(64) comment '字符集，默认 UTF-8',
   user_name            varchar(64) not null comment '数据库用户名',
   password             varchar(256) not null comment '数据库密码',
   user_id              int(11) not null comment '外键，用户 ID',
   primary key (id)
);

alter table mind_mark_db_for_process comment '需要进行处理的数据库，让用户自己配置。';

/*==============================================================*/
/* Table: mind_mark_embedding_log                               */
/*==============================================================*/
create table mind_mark_embedding_log
(
   id                   int(11) not null auto_increment,
   db_id                int(11),
   table_id             int(11) comment '表 ID ，记录已经处理过的表，此字段不与 file_id 共用，当 file_id 有值时，此字段应该无值',
   start_id             int(11) comment '已经处理到的行 ID ，此字段与 table_id 配合使用',
   file_id              int(11) comment '记录已经处理过的文件 ID，此字段不与 table_id 共用，当 table_id 有值时，此字段应该无值',
   index_id             int(11) comment '向量数据库中的索引 ID，用于记录向量化之后的数据存储在哪个索引中
            对于不同的表或者文件，用户可以指定索引',
   primary key (id)
);

alter table mind_mark_embedding_log comment '向量化记录';

/*==============================================================*/
/* Table: mind_mark_file_upload                                 */
/*==============================================================*/
create table mind_mark_file_upload
(
   id                   int(11) not null auto_increment comment '主键，自增',
   file_name            varchar(255) not null comment '文件名，实际存储此文件的名称，一般由系统通过 UUID 生成，防止同一目录中文件名冲突，此名称带有后缀',
   display_name         varchar(255) not null comment '文件的展示名称，一般取文件的原始名称，不带后缀',
   file_suffix          varchar(32) comment '文件的后缀',
   file_size            bigint default 0,
   path                 varchar(255) not null comment '文件在磁盘上的完整路径
            此字段不能与 URL 字段并存',
   url                  varchar(255) comment '访问此文件的路径，可以指向系统外部的 URL
            此字段不能和 path 字段并存',
   file_desc            varchar(255) default null,
   display_order        int(11) not null default 1 comment '显示顺序',
   up_time              datetime not null default current_timestamp,
   user_id              int(11) not null default 0,
   primary key (id)
);

alter table mind_mark_file_upload comment '维护用户上传的文件。';

/*==============================================================*/
/* Table: mind_mark_rbac_user                                   */
/*==============================================================*/
create table mind_mark_rbac_user
(
   user_id              int(11) not null auto_increment,
   user_name            varchar(64) not null,
   nick_name            varchar(64) not null,
   password             varchar(64) not null default '',
   salt                 varchar(32) default '',
   email                varchar(64) default '',
   cellphone            varchar(32) default '',
   gender               int(11) default 0 comment '0男 1女 2未知',
   city                 varchar(128),
   education            varchar(128),
   avatar_url           varchar(64) default '' comment '用户头像 URL',
   create_time          datetime default current_timestamp,
   status               int(11) default 1 comment '-1 特权用户不能删除 0正常 1禁用 2删除',
   remark               varchar(1024) default '',
   primary key (user_id)
);

alter table mind_mark_rbac_user comment '对于 MindMark 来说，总是会自动创建一个默认的用户叫做 mind-mark ，密码也是 mind-mark';

insert into mind_mark_rbac_user
(
    user_name,
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
    remark
)
values
(
    'mind-mark',            -- 用户名
    'mind-mark',            -- 昵称
    'mind-mark',            -- 密码（明文密码）
    '',                     -- salt（默认为空）
    '',                     -- email（默认为空）
    '',                     -- cellphone（默认为空）
    2,                      -- 性别（默认为未知，值为 2）
    '',                     -- city（默认为空）
    '',                     -- education（默认为空）
    '',                     -- avatar_url（默认为空）
    current_timestamp,      -- 创建时间（使用当前时间戳）
    0,                      -- 状态（默认为正常，值为 0）
    '默认用户 mind-mark'    -- 备注（可以为该用户提供说明）
);

/*==============================================================*/
/* Table: mind_mark_schema_for_process                          */
/*==============================================================*/
create table mind_mark_schema_for_process
(
   id                   int(11) not null auto_increment,
   db_id                int(11) not null,
   schema_name          varchar(128) not null,
   primary key (id)
);

alter table mind_mark_schema_for_process comment '维护进行处理的库名称，用户可以在 UI 界面上进行配置。';

/*==============================================================*/
/* Table: mind_mark_table_for_process                           */
/*==============================================================*/
create table mind_mark_table_for_process
(
   id                   int(11) not null auto_increment,
   schema_id            int(11) not null,
   table_name           varchar(128) not null comment '需要被处理的表名称',
   id_column            varchar(128) not null comment '主键字段的名称，假设需要被处理的表带有整数自增型的 id',
   primary key (id)
);

alter table mind_mark_table_for_process comment '维护需要进行向量化的表，用户可以在 UI 界面上进行配置。';

/*==============================================================*/
/* Table: mind_mark_user_index                                  */
/*==============================================================*/
create table mind_mark_user_index
(
   id                   int(11) not null auto_increment,
   user_id              int(11) not null,
   index_name           varchar(256) not null comment '索引名称',
   remark               varchar(1024),
   primary key (id)
);

alter table mind_mark_user_index comment '维护用户在 ElasticSearch 中创建的索引
对于 MindMark 来说，总是会自动创建一个默认的';

insert into mind_mark_user_index(id, user_id, index_name, remark)
values
(1, (select user_id from mind_mark_rbac_user where user_name = 'mind-mark'), 'mind-mark','MindMark 默认索引');


