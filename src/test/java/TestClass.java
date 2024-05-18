import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.WeatherService;
import cn.qianzhikang.service.impl.WeatherServiceImpl;

public class TestClass {
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherServiceImpl();
        Location location = new Location();
        location.setLongitude("120.64160");
        location.setLatitude("31.16040");
        weatherService.queryWeatherFor24WithLocation(location);

    }
}
