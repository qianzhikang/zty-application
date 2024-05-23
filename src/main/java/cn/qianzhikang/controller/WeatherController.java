package cn.qianzhikang.controller;

import cn.hutool.json.JSONUtil;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.entity.WeatherData;
import cn.qianzhikang.service.CityService;
import cn.qianzhikang.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qianzhikang
 */
@RestController
@RequestMapping("weather")
public class WeatherController {
    @Resource
    private CityService cityService;

    @Resource
    private WeatherService weatherService;
    @GetMapping("/{cityName}")
    public String getWuJiangWeather(@PathVariable String cityName){
        Location location = cityService.queryCityInfo(cityName);
        List<HourlyData> hourlyData = weatherService.queryWeatherFor24WithLocation(location);
        return JSONUtil.toJsonStr(WeatherData.builder().cityName(cityName).hourlyData(hourlyData).build());
    }
}
