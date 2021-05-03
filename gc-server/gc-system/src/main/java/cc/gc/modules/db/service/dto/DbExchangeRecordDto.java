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
public class DbExchangeRecordDto implements Serializable {

    /** 交易编号 */
    private String id;

    /** 交易日期yyyyMMdd */
    private String txnDate;

    /** 交易花费积分 */
    private Integer txnIntegral;

    /** 交易卡号 */
    private String txnCard;

    /** 兑换商品编号 */
    private String goodsNo;

    /** 商品兑换数量 */
    private Integer goodsCount;

    /** 商品兑换分类 */
    private String goodsType;

    /** 创建时间 */
    private Timestamp createTime;
}