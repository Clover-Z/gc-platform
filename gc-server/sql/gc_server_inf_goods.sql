create table inf_goods
(
    id          varchar(32)   not null comment '商品id'
        primary key,
    name        varchar(50)   not null comment '商品名称',
    type        varchar(20)   not null comment '商品分类',
    stock       int default 0 not null comment '商品数量',
    cost        int           not null comment '兑换所需积分',
    status      int           not null comment '状态：0-待上架；1-已上架；2-已下架',
    create_time timestamp     not null comment '创建时间'
)
    comment '兑换商品信息表';

INSERT INTO `gc-server`.inf_goods (id, name, type, stock, cost, status, create_time) VALUES ('G000001', '哇哈哈AD钙奶一箱', '食品', 99, 500, 1, '2021-05-03 15:40:41');