package cn.qianzhikang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.qianzhikang.cache.ConfigCache;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qianzhikang
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    /**
     * 获取24小时天气情况
     *
     * @param location 经纬度
     */
    @Override
    public List<HourlyData> queryWeatherFor24WithLocation(Location location) {
        // 拼接url
        String URL = ConfigCache.get("weatherHost") + "location=" + location.getLat() + "," + location.getLon() + "&key=" + ConfigCache.get("key");
        // 请求接口获取数据
        String res = HttpUtil.get(URL);
        // 处理返回的jsons
        JSONObject json = JSONUtil.parseObj(res);
        List<HourlyData> hourlyData = new ArrayList<>();
        List hourly = json.get("hourly", List.class);
        Assert.notEmpty(hourly, "未知地区");
        hourly.forEach(item -> hourlyData.add(BeanUtil.toBean(item, HourlyData.class)));
        return hourlyData;
    }

}
