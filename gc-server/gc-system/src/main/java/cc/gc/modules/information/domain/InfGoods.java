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
package cc.gc.modules.information.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
@Table(name="inf_goods")
public class InfGoods implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "商品id")
    private String id;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "商品名称")
    private String name;

    @Column(name = "type",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "商品分类")
    private String type;

    @Column(name = "stock",nullable = false)
    @NotNull
    @ApiModelProperty(value = "商品数量")
    private Integer stock;

    @Column(name = "cost",nullable = false)
    @NotNull
    @ApiModelProperty(value = "兑换所需积分")
    private Integer cost;

    @Column(name = "status",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态：0-待上架；1-已上架；2-已下架")
    private Integer status;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    public void copy(InfGoods source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}