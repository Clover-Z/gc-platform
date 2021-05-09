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
import cc.gc.modules.information.domain.InfDevice;
import cc.gc.modules.information.service.InfDeviceService;
import cc.gc.modules.information.service.dto.InfDeviceQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author Clover.z
* @date 2021-05-02
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "信息：设备信息维护管理")
@RequestMapping("/api/infDevice")
public class InfDeviceController {

    private final InfDeviceService infDeviceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('infDevice:list')")
    public void download(HttpServletResponse response, InfDeviceQueryCriteria criteria) throws IOException {
        infDeviceService.download(infDeviceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询设备信息维护")
    @ApiOperation("查询设备信息维护")
    @PreAuthorize("@el.check('infDevice:list')")
    public ResponseEntity<Object> query(InfDeviceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(infDeviceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增设备信息维护")
    @ApiOperation("新增设备信息维护")
    @PreAuthorize("@el.check('infDevice:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InfDevice resources){
        return new ResponseEntity<>(infDeviceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改设备信息维护")
    @ApiOperation("修改设备信息维护")
    @PreAuthorize("@el.check('infDevice:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody InfDevice resources){
        infDeviceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除设备信息维护")
    @ApiOperation("删除设备信息维护")
    @PreAuthorize("@el.check('infDevice:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        infDeviceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}