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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author Clover.z
* @date 2021-05-02
**/
@Data
public class InfCustomerDto implements Serializable {

    /** id */
    private String id;

    /** 持卡人姓名 */
    private String name;

    /** 卡号 */
    private String cardNo;

    /** 地址 */
    private String address;

    /** 积分 */
    private Long integral;

    /** 用户状态  1-正常；2-封禁；3-已删除 */
    private Integer status;

    /** 注册时间 */
    private Timestamp createTime;
}