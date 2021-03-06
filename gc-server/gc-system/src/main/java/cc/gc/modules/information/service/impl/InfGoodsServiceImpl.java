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
package cc.gc.modules.information.service.impl;

import cc.gc.modules.information.domain.InfGoods;
import cc.gc.modules.information.repository.InfGoodsRepository;
import cc.gc.modules.information.service.InfGoodsService;
import cc.gc.modules.information.service.dto.InfGoodsDto;
import cc.gc.modules.information.service.dto.InfGoodsQueryCriteria;
import cc.gc.modules.information.service.mapstruct.InfGoodsMapper;
import cc.gc.utils.FileUtil;
import cc.gc.utils.PageUtil;
import cc.gc.utils.QueryHelp;
import cc.gc.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author Clover.z
* @date 2021-05-02
**/
@Service
@RequiredArgsConstructor
public class InfGoodsServiceImpl implements InfGoodsService {

    private final InfGoodsRepository infGoodsRepository;
    private final InfGoodsMapper infGoodsMapper;

    @Override
    public Map<String,Object> queryAll(InfGoodsQueryCriteria criteria, Pageable pageable){
        Page<InfGoods> page = infGoodsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(infGoodsMapper::toDto));
    }

    @Override
    public List<InfGoodsDto> queryAll(InfGoodsQueryCriteria criteria){
        return infGoodsMapper.toDto(infGoodsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public InfGoodsDto findById(String id) {
        InfGoods infGoods = infGoodsRepository.findById(id).orElseGet(InfGoods::new);
        ValidationUtil.isNull(infGoods.getId(),"InfGoods","id",id);
        return infGoodsMapper.toDto(infGoods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfGoodsDto create(InfGoods resources) {
        return infGoodsMapper.toDto(infGoodsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InfGoods resources) {
        InfGoods infGoods = infGoodsRepository.findById(resources.getId()).orElseGet(InfGoods::new);
        ValidationUtil.isNull( infGoods.getId(),"InfGoods","id",resources.getId());
        infGoods.copy(resources);
        infGoodsRepository.save(infGoods);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            infGoodsRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<InfGoodsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InfGoodsDto infGoods : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商品名称", infGoods.getName());
            map.put("商品分类", infGoods.getType());
            map.put("商品数量", infGoods.getStock());
            map.put("兑换所需积分", infGoods.getCost());
            map.put("状态：0-待上架；1-已上架；2-已下架", infGoods.getStatus());
            map.put("创建时间", infGoods.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}