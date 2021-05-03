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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author Clover.z
* @date 2021-05-02
**/
@Entity
@Data
@Table(name="db_collection_record")
public class DbCollectionRecord implements Serializable {

    @Id
    @Column(name = "txn")
    @ApiModelProperty(value = "交易编号")
    private String txn;

    @Column(name = "txn_card",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "交易卡号")
    private String txnCard;

    @Column(name = "txn_date",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "交易日期 yyyyMMdd")
    private String txnDate;

    @Column(name = "gc_type",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "垃圾类别")
    private String gcType;

    @Column(name = "gc_weight",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "垃圾重量")
    private String gcWeight;

    @Column(name = "txn_integral",nullable = false)
    @NotNull
    @ApiModelProperty(value = "获得积分额")
    private Integer txnIntegral;

    @Column(name = "txn_device", nullable = false)
    @NotNull
    @ApiModelProperty(value = "交易设备号")
    private String txnDevice;

    @Column(name = "create_time",nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    public void copy(DbCollectionRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}