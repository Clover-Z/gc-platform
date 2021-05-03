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
import cc.gc.modules.information.domain.InfDevice;
import cc.gc.modules.information.repository.InfDeviceRepository;
import cc.gc.modules.information.service.InfDeviceService;
import cc.gc.modules.information.service.dto.InfDeviceDto;
import cc.gc.modules.information.service.dto.InfDeviceQueryCriteria;
import cc.gc.modules.information.service.mapstruct.InfDeviceMapper;
import cc.gc.utils.*;
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
public class InfDeviceServiceImpl implements InfDeviceService {

    private final InfDeviceRepository infDeviceRepository;
    private final InfDeviceMapper infDeviceMapper;

    @Override
    public Map<String,Object> queryAll(InfDeviceQueryCriteria criteria, Pageable pageable){
        Page<InfDevice> page = infDeviceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(infDeviceMapper::toDto));
    }

    @Override
    public List<InfDeviceDto> queryAll(InfDeviceQueryCriteria criteria){
        return infDeviceMapper.toDto(infDeviceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public InfDeviceDto findById(String id) {
        InfDevice infDevice = infDeviceRepository.findById(id).orElseGet(InfDevice::new);
        ValidationUtil.isNull(infDevice.getId(),"InfDevice","id",id);
        return infDeviceMapper.toDto(infDevice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfDeviceDto create(InfDevice resources) {
        InfDevice infDevice = infDeviceRepository.findById(resources.getId()).orElseGet(InfDevice::new);
        if (null != infDevice.getId()) {
            throw new EntityExistException(InfDevice.class, "设备id", resources.getId());
        }
        resources.setForDept(SecurityUtils.getCurrentUser().getUsername());
        return infDeviceMapper.toDto(infDeviceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InfDevice resources) {
        InfDevice infDevice = infDeviceRepository.findById(resources.getId()).orElseGet(InfDevice::new);
        ValidationUtil.isNull( infDevice.getId(),"InfDevice","id",resources.getId());
        infDevice.copy(resources);
        infDeviceRepository.save(infDevice);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            infDeviceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<InfDeviceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InfDeviceDto infDevice : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("设备描述", infDevice.getRemark());
            map.put("经度", infDevice.getLongitude());
            map.put("纬度", infDevice.getLatitude());
            map.put("设备详细地址", infDevice.getAddress());
            map.put("设备状态", infDevice.getStatus().equals("0") ? "未启用" : "已启用");
            map.put("设备负责人", infDevice.getPerson());
            map.put("负责人联系方式", infDevice.getPhone());
            map.put("设备归属部门", infDevice.getForDept());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}