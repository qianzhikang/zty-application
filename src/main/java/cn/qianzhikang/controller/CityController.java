package cn.qianzhikang.controller;

import cn.qianzhikang.RestResponse.RestResult;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.CityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author qianzhikang
 */
@RestController
@RequestMapping("city")
public class CityController {
    @Resource
    private CityService cityService;

    @GetMapping("/search/{cityName}")
    public RestResult getCityInfo(@PathVariable String cityName){
        Location location = cityService.queryCityInfo(cityName);
        return new RestResult<>().success(location);
    }
}
