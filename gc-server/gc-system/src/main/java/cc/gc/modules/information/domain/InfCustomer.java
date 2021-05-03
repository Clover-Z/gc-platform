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
@Table(name="inf_customer")
public class InfCustomer implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private String id;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "持卡人姓名")
    private String name;

    @Column(name = "card_no",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @Column(name = "address",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "地址")
    private String address;

    @Column(name = "integral",nullable = false)
    @ApiModelProperty(value = "积分")
    private Long integral;

    @Column(name = "status",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户状态  1-正常；2-封禁；3-已删除")
    private Integer status;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "注册时间")
    private Timestamp createTime;

    public void copy(InfCustomer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}