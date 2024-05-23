package cn.qianzhikang.service;

import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qianzhikang
 */

public interface WeatherService {

   List<HourlyData> queryWeatherFor24WithLocation(Location location);

}
