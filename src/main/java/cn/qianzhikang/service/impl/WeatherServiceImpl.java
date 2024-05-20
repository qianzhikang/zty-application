package cn.qianzhikang.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.qianzhikang.common.CommonData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.WeatherHourlyData;
import cn.qianzhikang.service.WeatherService;
import org.springframework.stereotype.Service;

/**
 * @author qianzhikang
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    /**
     * 获取24小时天气情况
     * @param location 经纬度
     */
    @Override
    public void queryWeatherFor24WithLocation(Location location) {
        String URL = CommonData.weatherHost + "location=" + location.getLon() + "," + location.getLat() + "&key=" + CommonData.key;
        String res = HttpUtil.get(URL);
        WeatherHourlyData data = JSONUtil.toBean(res, WeatherHourlyData.class);
        System.out.println(data);
    }

}
