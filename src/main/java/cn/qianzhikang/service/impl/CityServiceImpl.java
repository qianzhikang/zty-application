package cn.qianzhikang.service.impl;

import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.CityService;
import org.springframework.stereotype.Service;

/**
 * @author qianzhikang
 */
@Service
public class CityServiceImpl implements CityService {
    /**
     * 查询城市信息
     * @param cityName 城市名
     * @return 同一返回
     */
    @Override
    public Location queryCityInfo(String cityName) {
        return null;
    }
}
