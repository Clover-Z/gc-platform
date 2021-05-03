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
package cc.gc.modules.information.repository;

import cc.gc.modules.information.domain.InfCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @website https://el-admin.vip
* @author Clover.z
* @date 2021-05-02
**/
public interface InfCustomerRepository extends JpaRepository<InfCustomer, String>, JpaSpecificationExecutor<InfCustomer> {
    /**
    * 根据 CardNo 查询
    * @param cardNo /
    * @return /
    */
    InfCustomer findByCardNo(String cardNo);


    /**
     * 统计指定日期前用户数
     * @param date
     * @return
     */
    @Query(value = "select count(1) from inf_customer where create_time <= ?1", nativeQuery = true)
    Long countByCreateTime(String date);

}