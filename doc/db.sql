
alter table  classfy add column playTypeList
create table member_records_total
(
id int primary key auto_increment,
memberId int not null comment '会员id',
classfyId int comment '分类id',
money int comment '钱数',
lastUpdatetTime bigint comment '创建时间'
)

