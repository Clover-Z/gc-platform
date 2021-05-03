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
package cc.gc.modules.information.rest;

import cc.gc.annotation.Log;
import cc.gc.modules.information.domain.InfCustomer;
import cc.gc.modules.information.service.InfCustomerService;
import cc.gc.modules.information.service.dto.InfCustomerQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website https://el-admin.vip
* @author Clover.z
* @date 2021-05-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "客户信息维护管理")
@RequestMapping("/api/infCustomer")
public class InfCustomerController {

    private final InfCustomerService infCustomerService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('infCustomer:list')")
    public void download(HttpServletResponse response, InfCustomerQueryCriteria criteria) throws IOException {
        infCustomerService.download(infCustomerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询客户信息维护")
    @ApiOperation("查询客户信息维护")
    @PreAuthorize("@el.check('infCustomer:list')")
    public ResponseEntity<Object> query(InfCustomerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(infCustomerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增客户信息维护")
    @ApiOperation("新增客户信息维护")
    @PreAuthorize("@el.check('infCustomer:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InfCustomer resources){
        return new ResponseEntity<>(infCustomerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改客户信息维护")
    @ApiOperation("修改客户信息维护")
    @PreAuthorize("@el.check('infCustomer:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody InfCustomer resources){
        infCustomerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除客户信息维护")
    @ApiOperation("删除客户信息维护")
    @PreAuthorize("@el.check('infCustomer:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        infCustomerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}