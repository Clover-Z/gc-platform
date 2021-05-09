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
import cc.gc.modules.information.domain.InfGoods;
import cc.gc.modules.information.service.InfGoodsService;
import cc.gc.modules.information.service.dto.InfGoodsQueryCriteria;
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
@Api(tags = "信息：兑换商品信息维护管理")
@RequestMapping("/api/infGoods")
public class InfGoodsController {

    private final InfGoodsService infGoodsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('infGoods:list')")
    public void download(HttpServletResponse response, InfGoodsQueryCriteria criteria) throws IOException {
        infGoodsService.download(infGoodsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询兑换商品信息维护")
    @ApiOperation("查询兑换商品信息维护")
    @PreAuthorize("@el.check('infGoods:list')")
    public ResponseEntity<Object> query(InfGoodsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(infGoodsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增兑换商品信息维护")
    @ApiOperation("新增兑换商品信息维护")
    @PreAuthorize("@el.check('infGoods:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody InfGoods resources){
        return new ResponseEntity<>(infGoodsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改兑换商品信息维护")
    @ApiOperation("修改兑换商品信息维护")
    @PreAuthorize("@el.check('infGoods:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody InfGoods resources){
        infGoodsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除兑换商品信息维护")
    @ApiOperation("删除兑换商品信息维护")
    @PreAuthorize("@el.check('infGoods:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody String[] ids) {
        infGoodsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}