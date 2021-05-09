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
import cc.gc.modules.db.domain.DbExchangeRecord;
import cc.gc.modules.db.repository.DbExchangeRecordRepository;
import cc.gc.modules.db.service.DbExchangeRecordService;
import cc.gc.modules.db.service.dto.DbExchangeRecordDto;
import cc.gc.modules.db.service.dto.DbExchangeRecordQueryCriteria;
import cc.gc.modules.db.service.mapstruct.DbExchangeRecordMapper;
import cc.gc.modules.information.domain.InfCustomer;
import cc.gc.modules.information.domain.InfGoods;
import cc.gc.modules.information.repository.InfCustomerRepository;
import cc.gc.modules.information.repository.InfGoodsRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class DbExchangeRecordServiceImpl implements DbExchangeRecordService {

    private final DbExchangeRecordRepository dbExchangeRecordRepository;
    private final DbExchangeRecordMapper dbExchangeRecordMapper;

    private final InfGoodsRepository infGoodsRepository;
    private final InfCustomerRepository infCustomerRepository;

    @Override
    public Map<String,Object> queryAll(DbExchangeRecordQueryCriteria criteria, Pageable pageable){
        Page<DbExchangeRecord> page = dbExchangeRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dbExchangeRecordMapper::toDto));
    }

    @Override
    public List<DbExchangeRecordDto> queryAll(DbExchangeRecordQueryCriteria criteria){
        return dbExchangeRecordMapper.toDto(dbExchangeRecordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DbExchangeRecordDto findById(String id) {
        DbExchangeRecord dbExchangeRecord = dbExchangeRecordRepository.findById(id).orElseGet(DbExchangeRecord::new);
        ValidationUtil.isNull(dbExchangeRecord.getId(),"DbExchangeRecord","id",id);
        return dbExchangeRecordMapper.toDto(dbExchangeRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DbExchangeRecordDto create(DbExchangeRecord resources) {
        resources.setId(IdUtil.simpleUUID());
        resources.setTxnDate((DateTimeFormatter.ofPattern("yyyyMMdd")).format(LocalDateTime.now()));

        synchronized (this) {
            //验证商品
            InfGoods goods = infGoodsRepository.findById(resources.getGoodsNo()).orElseGet(InfGoods::new);
            if (null == goods.getStatus() || goods.getStatus() != 1) {
                throw new BadRequestException("无效的商品编号或商品未上架");
            }
            resources.setGoodsType(goods.getType());

            //验证卡号
            InfCustomer customer = infCustomerRepository.findByCardNo(resources.getTxnCard());
            if (null == customer || customer.getStatus() != 1) {
                throw new BadRequestException("无效的交易卡号或账户状态存在异常");
            }

            //扣积分
            int totalIntegral = goods.getCost() * resources.getGoodsCount();
            if (customer.getIntegral() < totalIntegral) {
                throw new BadRequestException("账户积分余额不足");
            }
            customer.setIntegral(customer.getIntegral() - totalIntegral);
            infCustomerRepository.save(customer);
            resources.setTxnIntegral(totalIntegral);

            //扣库存
            if (goods.getStock() < resources.getGoodsCount()) {
                throw new BadRequestException("商品库存不足");
            }
            goods.setStock(goods.getStock() - resources.getGoodsCount());
            infGoodsRepository.save(goods);

            return dbExchangeRecordMapper.toDto(dbExchangeRecordRepository.save(resources));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DbExchangeRecord resources) {
        DbExchangeRecord dbExchangeRecord = dbExchangeRecordRepository.findById(resources.getId()).orElseGet(DbExchangeRecord::new);
        ValidationUtil.isNull( dbExchangeRecord.getId(),"DbExchangeRecord","id",resources.getId());
        dbExchangeRecord.copy(resources);
        dbExchangeRecordRepository.save(dbExchangeRecord);
    }

    @Override
    public void deleteAll(String[] ids) {
        for (String id : ids) {
            dbExchangeRecordRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DbExchangeRecordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DbExchangeRecordDto dbExchangeRecord : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("交易日期yyyyMMdd", dbExchangeRecord.getTxnDate());
            map.put("交易花费积分", dbExchangeRecord.getTxnIntegral());
            map.put("交易卡号", dbExchangeRecord.getTxnCard());
            map.put("兑换商品编号", dbExchangeRecord.getGoodsNo());
            map.put("商品兑换数量", dbExchangeRecord.getGoodsCount());
            map.put("商品兑换分类", dbExchangeRecord.getGoodsType());
            map.put("创建时间", dbExchangeRecord.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}