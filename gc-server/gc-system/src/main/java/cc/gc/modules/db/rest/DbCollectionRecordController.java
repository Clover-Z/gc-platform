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
import cc.gc.modules.db.domain.DbCollectionRecord;
import cc.gc.modules.db.service.DbCollectionRecordService;
import cc.gc.modules.db.service.dto.DbCollectionRecordQueryCriteria;
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
@Api(tags = "平台开放：投放记录维护管理")
@RequestMapping("/api/dbCollectionRecord")
public class DbCollectionRecordController {

    private final DbCollectionRecordService dbCollectionRecordService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dbCollectionRecord:list')")
    public void download(HttpServletResponse response, DbCollectionRecordQueryCriteria criteria) throws IOException {
        dbCollectionRecordService.download(dbCollectionRecordService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询投放记录维护")
    @ApiOperation("查询投放记录维护")
    @PreAuthorize("@el.check('dbCollectionRecord:list')")
    public ResponseEntity<Object> query(DbCollectionRecordQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(dbCollectionRecordService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增投放记录维护")
    @ApiOperation("新增投放记录维护")
    @PreAuthorize("@el.check('dbCollectionRecord:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DbCollectionRecord resources){
        return new ResponseEntity<>(dbCollectionRecordService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改投放记录维护")
    @ApiOperation("修改投放记录维护")
    @PreAuthorize("@el.check('dbCollectionRecord:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DbCollectionRecord resources){
        dbCollectionRecordService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除投放记录维护")
    @ApiOperation("删除投放记录维护")
    @PreAuthorize("@el.check('dbCollectionRecord:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        dbCollectionRecordService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}