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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
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
@Table(name="inf_device")
public class InfDevice implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "设备号")
    private String id;

    @Column(name = "remark",nullable = false)
    @ApiModelProperty(value = "设备描述")
    private String remark;

    @Column(name = "longitude",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "经度")
    private String longitude;

    @Column(name = "latitude",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "纬度")
    private String latitude;

    @Column(name = "address",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "设备详细地址")
    private String address;

    @Column(name = "status",nullable = false)
    @NotNull
    @ApiModelProperty(value = "0-未启用；1-已启用")
    private Integer status;

    @Column(name = "person")
    @ApiModelProperty(value = "设备负责人")
    private String person;

    @Column(name = "phone")
    @ApiModelProperty(value = "负责人联系方式")
    private String phone;

    @Column(name = "for_dept",nullable = false)
    @ApiModelProperty(value = "设备归属部门")
    private String forDept;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    public void copy(InfDevice source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}