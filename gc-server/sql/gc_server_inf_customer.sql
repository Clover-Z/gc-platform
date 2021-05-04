create table inf_customer
(
    id          char(32)         not null comment 'id'
        primary key,
    name        varchar(20)      not null comment '持卡人姓名',
    card_no     varchar(32)      not null comment '卡号',
    address     varchar(200)     not null comment '地址',
    integral    bigint default 0 not null comment '积分',
    status      int              not null comment '用户状态  1-正常；2-封禁；3-已删除',
    create_time timestamp        not null comment '注册时间',
    constraint inf_customer_card_no_uindex
        unique (card_no)
)
    comment '客户信息表';

INSERT INTO `gc-server`.inf_customer (id, name, card_no, address, integral, status, create_time) VALUES ('c5731866fa3143f2a4e845485f1e4731', '大黄狗', '2100000000001', '就是一个普通的地址', 350, 1, '2021-05-03 15:33:50');