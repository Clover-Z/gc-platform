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
package cc.gc.modules.information.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author Clover.z
* @date 2021-05-02
**/
@Data
public class InfDeviceDto implements Serializable {

    /** 设备号 */
    private String id;

    /** 设备描述 */
    private String remark;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /** 设备详细地址 */
    private String address;

    /** 0-未启用；1-已启用 */
    private Integer status;

    /** 设备负责人 */
    private String person;

    /** 负责人联系方式 */
    private String phone;

    /** 设备归属部门 */
    private String forDept;

    /** 创建时间 */
    private Timestamp createTime;
}