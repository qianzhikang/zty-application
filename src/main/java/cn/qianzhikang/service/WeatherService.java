package cn.qianzhikang.service;

import cn.qianzhikang.entity.Location;
import org.springframework.stereotype.Service;

/**
 * @author qianzhikang
 */

public interface WeatherService {

   void queryWeatherFor24WithLocation(Location location);

}
