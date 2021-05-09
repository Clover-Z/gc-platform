package cc.gc.modules.report;

import cc.gc.annotation.AnonymousAccess;
import cc.gc.modules.db.repository.DbCollectionRecordRepository;
import cc.gc.modules.db.repository.DbExchangeRecordRepository;
import cc.gc.modules.information.repository.InfCustomerRepository;
import cc.gc.modules.information.repository.InfDeviceRepository;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Api(tags = "统计：报表接口")
@RequestMapping("/api/report")
public class ReportController {

    private final InfCustomerRepository infCustomerRepository;
    private final InfDeviceRepository infDeviceRepository;
    private final DbExchangeRecordRepository dbExchangeRecordRepository;
    private final DbCollectionRecordRepository dbCollectionRecordRepository;


    @ApiOperation("累计报表数统计")
    @GetMapping("/total")
    @AnonymousAccess
    public ResponseEntity<Object> getTotal() {
        List<Long> list = new ArrayList<>();
        //累计用户统计
        list.add(infCustomerRepository.count());
        //累计设备
        list.add(infDeviceRepository.count());
        //累计发放积分
        list.add(dbCollectionRecordRepository.sumAllTxnIntegral());
        //累计兑换商品数
        list.add(dbExchangeRecordRepository.sumAllTxnGoodsCount());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }



    @AnonymousAccess
    @ApiOperation("type:1-累计用户; 2-累计设备; 3-累计发放积分; 4-累计兑换商品数")
    @GetMapping("/totalLineChart")
    public ResponseEntity<Object> getLineChart(@RequestParam Integer type) {
        Function<String, Long> function;
        switch (type) {
            case 1: function = infCustomerRepository::countByCreateTime; break;
            case 2: function = infDeviceRepository::countByCreateTime; break;
            case 3: function = dbCollectionRecordRepository::sumTxnIntegralByCreateTime; break;
            case 4: function = dbExchangeRecordRepository :: sumTxnGoodsCountByCreateTime; break;
            default: return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(getLineChartData(function), HttpStatus.OK);
    }


    private JSONObject getLineChartData(Function<String, Long> function) {
        JSONObject obj = new JSONObject();
        List<Long> list = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        getLast7DaysDate().forEach(date -> {
            Long r = function.apply(date);
            list.add(r == null ? 0L : r);
            dateList.add(Integer.parseInt(date.substring(4, 6)) + "月" + Integer.parseInt(date.substring(6, 8)) + "日");
        });
        obj.put("actualData", list);
        obj.put("xNames", dateList);
        return obj;
    }


    private List<String> getLast7DaysDate() {
        List<String> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(1); //fix timestamp类型 数据库小于等于结果不包含当天
        for (int i=6; i>=0; i--) {
            list.add(now.minusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        return list;
    }

}
