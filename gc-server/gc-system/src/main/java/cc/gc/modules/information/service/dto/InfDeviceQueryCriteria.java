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

import cc.gc.annotation.Query;
import lombok.Data;

/**
* @website https://el-admin.vip
* @author Clover.z
* @date 2021-05-02
**/
@Data
public class InfDeviceQueryCriteria{

    /** 精确 */
    @Query
    private Integer id;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String remark;

    /** 精确 */
    @Query
    private String longitude;

    /** 精确 */
    @Query
    private String latitude;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String address;

    /** 精确 */
    @Query
    private Integer status;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String person;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String phone;

    /** 精确 */
    @Query
    private String forDept;
}