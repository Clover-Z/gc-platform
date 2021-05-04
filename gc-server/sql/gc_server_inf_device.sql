create table inf_device
(
    id          varchar(32)  not null comment '设备号'
        primary key,
    remark      varchar(100) not null comment '设备描述',
    longitude   varchar(10)  not null comment '经度',
    latitude    varchar(10)  not null comment '纬度',
    address     varchar(100) not null comment '设备详细地址',
    status      int          not null comment '0-未启用；1-已启用',
    person      varchar(20)  null comment '设备负责人',
    phone       varchar(20)  null comment '负责人联系方式',
    for_dept    varchar(32)  not null comment '设备归属部门',
    create_time timestamp    not null comment '创建时间'
)
    comment '设备信息表';

INSERT INTO `gc-server`.inf_device (id, remark, longitude, latitude, address, status, person, phone, for_dept, create_time) VALUES ('A0001', '设备a0001', '140', '12.03', '详细地址1234', 0, '小明', '19900001111', 'admin', '2021-05-03 15:33:13');