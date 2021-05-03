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
package cc.gc.modules.db.service.dto;

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
public class DbCollectionRecordDto implements Serializable {

    /** 交易编号 */
    private String txn;

    /** 交易卡号 */
    private String txnCard;

    /** 交易日期 yyyyMMdd */
    private String txnDate;

    /** 垃圾类别 */
    private String gcType;

    /** 垃圾重量 */
    private String gcWeight;

    /** 获得积分额 */
    private Integer txnIntegral;

    /** 交易设备号 */
    private String txnDevice;

    /** 创建时间 */
    private Timestamp createTime;
}