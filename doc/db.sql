
alter table  classfy add column playTypeList
create table member_records_total
(
id int primary key auto_increment,
memberId int not null comment '会员id',
classfyId int comment '分类id',
money DECIMAL (10,2) comment '钱数',
type int comment '1-统计,2-扣除'
createTime int comment '创建时间',
lastUpdatetTime bigint comment '创建时间'
)

