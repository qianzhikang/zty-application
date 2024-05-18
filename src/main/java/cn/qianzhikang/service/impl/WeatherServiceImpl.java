package cn.qianzhikang.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.WeatherHourlyData;
import cn.qianzhikang.service.WeatherService;

/**
 * @author qianzhikang
 */
public class WeatherServiceImpl implements WeatherService {
    /**
     * 获取24小时天气情况
     * @param location 经纬度
     */
    @Override
    public void queryWeatherFor24WithLocation(Location location) {
        String baseUrl = "https://devapi.qweather.com/v7/weather/24h?";
        String key = "7b7ad83067064970b73c13b816b026bd";
        String URL = baseUrl + "location=" + location.getLongitude() + "," + location.getLatitude() + "&key=" + key;
//        String res = HttpUtil.get("https://devapi.qweather.com/v7/weather/24h?location=101010100&key=7b7ad83067064970b73c13b816b026bd");
        String res = HttpUtil.get(URL);
        WeatherHourlyData data = JSONUtil.toBean(res, WeatherHourlyData.class);
        System.out.println(data);
    }

}
