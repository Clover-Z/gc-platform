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
package cc.gc.modules.db.rest;

import cc.gc.annotation.Log;
import cc.gc.modules.db.domain.DbExchangeRecord;
import cc.gc.modules.db.service.DbExchangeRecordService;
import cc.gc.modules.db.service.dto.DbExchangeRecordQueryCriteria;
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
@Api(tags = "平台开放：积分兑换记录维护管理")
@RequestMapping("/api/dbExchangeRecord")
public class DbExchangeRecordController {

    private final DbExchangeRecordService dbExchangeRecordService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dbExchangeRecord:list')")
    public void download(HttpServletResponse response, DbExchangeRecordQueryCriteria criteria) throws IOException {
        dbExchangeRecordService.download(dbExchangeRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询积分兑换记录维护")
    @ApiOperation("查询积分兑换记录维护")
    @PreAuthorize("@el.check('dbExchangeRecord:list')")
    public ResponseEntity<Object> query(DbExchangeRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dbExchangeRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增积分兑换记录维护")
    @ApiOperation("新增积分兑换记录维护")
    @PreAuthorize("@el.check('dbExchangeRecord:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DbExchangeRecord resources){
        return new ResponseEntity<>(dbExchangeRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改积分兑换记录维护")
    @ApiOperation("修改积分兑换记录维护")
    @PreAuthorize("@el.check('dbExchangeRecord:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DbExchangeRecord resources){
        dbExchangeRecordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除积分兑换记录维护")
    @ApiOperation("删除积分兑换记录维护")
    @PreAuthorize("@el.check('dbExchangeRecord:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        dbExchangeRecordService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}