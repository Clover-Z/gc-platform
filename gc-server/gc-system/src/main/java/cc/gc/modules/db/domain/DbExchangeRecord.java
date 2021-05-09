/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package cc.gc.modules.db.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author Clover.z
* @date 2021-05-02
**/
@Entity
@Data
@Table(name="db_exchange_record")
public class DbExchangeRecord implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "交易编号")
    private String id;

    @Column(name = "txn_date",nullable = false)
    @ApiModelProperty(value = "交易日期yyyyMMdd")
    private String txnDate;

    @Column(name = "txn_integral",nullable = false)
    @ApiModelProperty(value = "交易花费积分")
    private Integer txnIntegral;

    @Column(name = "txn_card",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "交易卡号")
    private String txnCard;

    @Column(name = "goods_no",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "兑换商品编号")
    private String goodsNo;

    @Column(name = "goods_count",nullable = false)
    @NotNull
    @ApiModelProperty(value = "商品兑换数量")
    private Integer goodsCount;

    @Column(name = "goods_type",nullable = false)
    @ApiModelProperty(value = "商品兑换分类")
    private String goodsType;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    public void copy(DbExchangeRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}