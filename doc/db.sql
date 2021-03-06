
alter table  classify add column smallType  varchar(255)  comment '游戏分类';
drop table if EXISTS member_records_total;
create table member_records_total
(
id int primary key auto_increment,
memberId int not null comment '会员id',
classifyId int comment '分类id',
money DECIMAL (16,2) comment '钱数',
createTime bigint comment '创建时间',
feeTime bigint comment '返水的时间'
);

drop table if EXISTS branch_money_log;
create table branch_money_log
(
id int primary key auto_increment,
money DECIMAL (16,2) comment '钱数',
branchId int comment '会员id',
classifyId int comment '1-统计,2-扣除',
createTime bigint comment '创建时间'
);

drop table if EXISTS member_money_log;
create table member_money_log
(
id int primary key auto_increment,
money DECIMAL (16,2) comment '钱数',
memberId int comment '会员id',
type int comment '1-统计,2-扣除',
beforeMoney DECIMAL (16,2) comment '前值',
afterMoney DECIMAL (16,2) comment '后值',
memo varchar(500) comment '说明',
createTime bigint comment '创建时间'
);

drop table if EXISTS admin_money_log;
create table admin_money_log
(
id int primary key auto_increment,
type int comment '类型',
name varchar(255) comment '',
realName varchar(255) comment '',
money DECIMAL (16,2) comment '钱数',
createTime bigint comment '创建时间'
);


drop table if EXISTS back_log;
create table back_log
(
id int primary key auto_increment,
lastBackTime bigint comment '最后返现时间',
insertTime bigint comment '写入时间'
);
