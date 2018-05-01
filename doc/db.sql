
alter table  classify modify column type  varchar(255)  comment '游戏分类';
create table member_records_total
(
id int primary key auto_increment,
memberId int not null comment '会员id',
classifyId int comment '分类id',
money DECIMAL (16,2) comment '钱数',
type int comment '1-统计,2-扣除',
createTime bigint comment '创建时间',
lastUpdatetTime bigint comment '创建时间'
);

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
