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

import cc.gc.exception.EntityExistException;
import cc.gc.modules.information.domain.InfCustomer;
import cc.gc.modules.information.repository.InfCustomerRepository;
import cc.gc.modules.information.service.InfCustomerService;
import cc.gc.modules.information.service.dto.InfCustomerDto;
import cc.gc.modules.information.service.dto.InfCustomerQueryCriteria;
import cc.gc.modules.information.service.mapstruct.InfCustomerMapper;
import cc.gc.utils.FileUtil;
import cc.gc.utils.PageUtil;
import cc.gc.utils.QueryHelp;
import cc.gc.utils.ValidationUtil;
import cn.hutool.core.util.IdUtil;
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
public class InfCustomerServiceImpl implements InfCustomerService {

    private final InfCustomerRepository infCustomerRepository;
    private final InfCustomerMapper infCustomerMapper;

    @Override
    public Map<String,Object> queryAll(InfCustomerQueryCriteria criteria, Pageable pageable){
        Page<InfCustomer> page = infCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(infCustomerMapper::toDto));
    }

    @Override
    public List<InfCustomerDto> queryAll(InfCustomerQueryCriteria criteria){
        return infCustomerMapper.toDto(infCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public InfCustomerDto findById(String id) {
        InfCustomer infCustomer = infCustomerRepository.findById(id).orElseGet(InfCustomer::new);
        ValidationUtil.isNull(infCustomer.getId(),"InfCustomer","id",id);
        return infCustomerMapper.toDto(infCustomer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfCustomerDto create(InfCustomer resources) {
        resources.setId(IdUtil.simpleUUID()); 
        if(infCustomerRepository.findByCardNo(resources.getCardNo()) != null){
            throw new EntityExistException(InfCustomer.class,"cardNo",resources.getCardNo());
        }
        if (null == resources.getIntegral()) resources.setIntegral(0L);
        return infCustomerMapper.toDto(infCustomerRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InfCustomer resources) {
        InfCustomer infCustomer = infCustomerRepository.findById(resources.getId()).orElseGet(InfCustomer::new);
        ValidationUtil.isNull( infCustomer.getId(),"InfCustomer","id",resources.getId());
        InfCustomer infCustomer1 = infCustomerRepository.findByCardNo(resources.getCardNo());
        if(infCustomer1 != null && !infCustomer1.getId().equals(infCustomer.getId())){
            throw new EntityExistException(InfCustomer.class,"cardNo",resources.getCardNo());
        }
        infCustomer.copy(resources);
        infCustomerRepository.save(infCustomer);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            infCustomerRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<InfCustomerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InfCustomerDto infCustomer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("持卡人姓名", infCustomer.getName());
            map.put("卡号", infCustomer.getCardNo());
            map.put("地址", infCustomer.getAddress());
            map.put("积分", infCustomer.getIntegral());
            map.put("用户状态  1-正常；2-封禁；3-已删除", infCustomer.getStatus());
            map.put("注册时间", infCustomer.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}