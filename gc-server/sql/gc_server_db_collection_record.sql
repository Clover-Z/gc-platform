create table db_collection_record
(
    txn          varchar(32) not null comment '交易编号'
        primary key,
    txn_card     varchar(32) not null comment '交易卡号',
    txn_date     char(8)     not null comment '交易日期 yyyyMMdd',
    gc_type      varchar(10) not null comment '垃圾类别',
    gc_weight    varchar(20) not null comment '垃圾重量',
    txn_integral int         not null comment '获得积分额',
    txn_device   varchar(32) not null comment '交易设备号',
    create_time  timestamp   not null comment '创建时间'
);

INSERT INTO `gc-server`.db_collection_record (txn, txn_card, txn_date, gc_type, gc_weight, txn_integral, txn_device, create_time) VALUES ('20211010101001', '2100000000001', '20210503', '衣物', '10', 300, 'A0001', '2021-05-01 16:00:24');
INSERT INTO `gc-server`.db_collection_record (txn, txn_card, txn_date, gc_type, gc_weight, txn_integral, txn_device, create_time) VALUES ('345678945678', '2100000000001', '20210502', '木材', '20', 550, 'A0001', '2021-05-03 16:04:18');