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
public class InfGoodsDto implements Serializable {

    /** 商品id */
    private String id;

    /** 商品名称 */
    private String name;

    /** 商品分类 */
    private String type;

    /** 商品数量 */
    private Integer stock;

    /** 兑换所需积分 */
    private Integer cost;

    /** 状态：0-待上架；1-已上架；2-已下架 */
    private Integer status;

    /** 创建时间 */
    private Timestamp createTime;
}