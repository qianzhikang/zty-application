import cn.hutool.extra.mail.MailUtil;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.WeatherService;
import cn.qianzhikang.service.impl.WeatherServiceImpl;

public class TestClass {
    public static void main(String[] args) {
//        WeatherService weatherService = new WeatherServiceImpl();
//        Location location = new Location();
//        location.setLongitude("120.64160");
//        location.setLatitude("31.16040");
//        weatherService.queryWeatherFor24WithLocation(location);
        MailUtil.send("957463620@qq.com", "测试", "邮件来自Hutool测试", false);


    }
}
