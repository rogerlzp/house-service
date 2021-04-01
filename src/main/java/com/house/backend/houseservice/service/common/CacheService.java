package com.house.backend.houseservice.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 缓存接口
 *
 * @author Helen.Chen
 * @version 2.5
 * @date 2020/6/04
 */
@Slf4j
@Service("cacheService")
public class CacheService {
//    @Autowired
//   private RefWorkdayInfoExtendMapper refWorkdayInfoExtendMapper;
//    /**
//     * 将日历表缓存到redis
//     */
//    public void loadRefWorkdayInfoCache() {
//        List<RefWorkdayInfo> refWorkdayInfoList = refWorkdayInfoExtendMapper.selectByCondition(new RefWorkdayInfo());
//        Map<String, RefWorkdayInfo> refWorkdayInfo = new HashMap<>();
//        Map<String, String> dateAndWorkdayOrder = new HashMap<>();
//
//        RedisUtil instance = RedisUtil.getInsatance();
//
//        instance.remove(CacheTableEnum.RefWorkdayInfo.getValue());
//        instance.remove(CacheTableEnum.DateAndWorkdayOrder.getValue());
//        instance.remove(CacheTableEnum.WorkdayRange.getValue());
//        instance.remove(CacheTableEnum.DateParseResult.getValue());
//
//        refWorkdayInfoList.stream().forEach(item -> {
//            refWorkdayInfo.put(item.getCalDate(), item);
//            dateAndWorkdayOrder.put(item.getWorkDayOrder().toString(), item.getCalDate());
//        });
//        instance.addMap("RefWorkdayInfo", refWorkdayInfo);
//        instance.addMap("DateAndWorkdayOrder", dateAndWorkdayOrder);
//        log.info("init [RefWorkdayInfo] cacheData success");
//        log.info("init [yearAndWorkdayOrder] cacheData success");
//
//        // 将日历表中的最大日期加载到缓存
//        WorkDayRange workDayRange = refWorkdayInfoExtendMapper.queryMaxAndMinDay();
//        Map<String, String> workdayRangeMap = new HashMap<>();
//        workdayRangeMap.put("minDate", workDayRange.getMinDate());
//        workdayRangeMap.put("maxDate", workDayRange.getMaxDate());
//        instance.addMap("WorkdayRange", workdayRangeMap);
//        log.info("init [WorkdayRange] cacheData success");
//        // 把日期规则解析加入缓存
//        Map<String, String> dateParseMap = new HashMap<>();
//        instance.addMap("DateParseResult", dateParseMap);
//        log.info("init [DateParseResult] cacheData success");
//    }

}
