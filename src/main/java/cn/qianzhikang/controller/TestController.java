package cn.qianzhikang.controller;

import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qianzhikang
 */
@RestController
public class TestController {
    @Resource
    private WeatherService weatherService;

    @GetMapping("test")
    public String test(){
        return "hello world!";
    }

    @GetMapping("wujiang")
    public String getWuJiangWeather(){
        Location location = new Location();
        location.setLongitude("120.64160");
        location.setLatitude("31.16040");
        weatherService.queryWeatherFor24WithLocation(location);
        return "success";
    }
}
