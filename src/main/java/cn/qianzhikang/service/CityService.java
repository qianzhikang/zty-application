package cn.qianzhikang.service;

import cn.qianzhikang.entity.Location;

/**
 * @author qianzhikang
 */
public interface CityService {

    /**
     * 查询城市信息
     * @param cityName 城市名
     * @return 同一返回
     */
    Location queryCityInfo(String cityName);
}
