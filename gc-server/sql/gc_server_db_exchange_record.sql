create table db_exchange_record
(
    id           varchar(32) not null comment '交易编号'
        primary key,
    txn_date     char(8)     not null comment '交易日期yyyyMMdd',
    txn_integral int         not null comment '交易花费积分',
    txn_card     varchar(32) not null comment '交易卡号',
    goods_no     varchar(32) not null comment '兑换商品编号',
    goods_count  int         not null comment '商品兑换数量',
    goods_type   varchar(10) not null comment '商品兑换分类',
    create_time  timestamp   not null comment '创建时间'
)
    comment '积分兑换记录表';

INSERT INTO `gc-server`.db_exchange_record (id, txn_date, txn_integral, txn_card, goods_no, goods_count, goods_type, create_time) VALUES ('34567890', '20210503', 500, '2100000000001', 'G000001', 1, '食品', '2021-05-03 16:30:57');