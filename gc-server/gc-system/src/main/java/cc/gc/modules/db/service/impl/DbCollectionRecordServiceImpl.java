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
package cc.gc.modules.db.service.impl;

import cc.gc.exception.BadRequestException;
import cc.gc.modules.db.domain.DbCollectionRecord;
import cc.gc.modules.db.repository.DbCollectionRecordRepository;
import cc.gc.modules.db.service.DbCollectionRecordService;
import cc.gc.modules.db.service.dto.DbCollectionRecordDto;
import cc.gc.modules.db.service.dto.DbCollectionRecordQueryCriteria;
import cc.gc.modules.db.service.mapstruct.DbCollectionRecordMapper;
import cc.gc.modules.information.domain.InfCustomer;
import cc.gc.modules.information.domain.InfDevice;
import cc.gc.modules.information.repository.InfCustomerRepository;
import cc.gc.modules.information.repository.InfDeviceRepository;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
public class DbCollectionRecordServiceImpl implements DbCollectionRecordService {

    private final DbCollectionRecordRepository dbCollectionRecordRepository;
    private final DbCollectionRecordMapper dbCollectionRecordMapper;

    private final InfCustomerRepository infCustomerRepository;
    private final InfDeviceRepository infDeviceRepository;

    @Override
    public Map<String,Object> queryAll(DbCollectionRecordQueryCriteria criteria, Pageable pageable){
        Page<DbCollectionRecord> page = dbCollectionRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dbCollectionRecordMapper::toDto));
    }

    @Override
    public List<DbCollectionRecordDto> queryAll(DbCollectionRecordQueryCriteria criteria){
        return dbCollectionRecordMapper.toDto(dbCollectionRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DbCollectionRecordDto findById(String txn) {
        DbCollectionRecord dbCollectionRecord = dbCollectionRecordRepository.findById(txn).orElseGet(DbCollectionRecord::new);
        ValidationUtil.isNull(dbCollectionRecord.getTxn(),"DbCollectionRecord","txn",txn);
        return dbCollectionRecordMapper.toDto(dbCollectionRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DbCollectionRecordDto create(DbCollectionRecord resources) {
        DbCollectionRecord r = dbCollectionRecordRepository.findById(resources.getTxn()).orElseGet(DbCollectionRecord::new);
        if (r.getTxn() != null) {
            //幂等
            return dbCollectionRecordMapper.toDto(r);
        }

        //验证设备号
        InfDevice device = infDeviceRepository.findById(resources.getTxnDevice()).orElseGet(InfDevice::new);
        if (device.getStatus() == null || device.getStatus() == 0){
            throw new BadRequestException("无效的设备或设备未启用");
        }

        //验证卡号
        InfCustomer customer = infCustomerRepository.findByCardNo(resources.getTxnCard());
        if (null == customer || customer.getStatus() != 1) {
            throw new BadRequestException("无效的交易卡号或账号状态不正常");
        }

        //加积分
        customer.setIntegral(customer.getIntegral() + resources.getTxnIntegral());
        infCustomerRepository.save(customer);

        resources.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        return dbCollectionRecordMapper.toDto(dbCollectionRecordRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DbCollectionRecord resources) {
        DbCollectionRecord dbCollectionRecord = dbCollectionRecordRepository.findById(resources.getTxn()).orElseGet(DbCollectionRecord::new);
        ValidationUtil.isNull( dbCollectionRecord.getTxn(),"DbCollectionRecord","id",resources.getTxn());
        dbCollectionRecord.copy(resources);
        dbCollectionRecordRepository.save(dbCollectionRecord);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String txn : ids) {
            dbCollectionRecordRepository.deleteById(txn);
        }
    }

    @Override
    public void download(List<DbCollectionRecordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DbCollectionRecordDto dbCollectionRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("交易卡号", dbCollectionRecord.getTxnCard());
            map.put("交易日期 yyyyMMdd", dbCollectionRecord.getTxnDate());
            map.put("垃圾类别", dbCollectionRecord.getGcType());
            map.put("垃圾重量", dbCollectionRecord.getGcWeight());
            map.put("获得积分额", dbCollectionRecord.getTxnIntegral());
            map.put("创建时间", dbCollectionRecord.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}