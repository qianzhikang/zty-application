package cn.qianzhikang.schedule;

import cn.qianzhikang.ding.DingTalkMsg;
import cn.qianzhikang.entity.CitySetting;
import cn.qianzhikang.entity.HourlyData;
import cn.qianzhikang.entity.Location;
import cn.qianzhikang.service.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qianzhikang
 */
@Component
public class TaskRunner {
    @Resource
    private WeatherService weatherService;

    @Resource
    private DingTalkMsg dingTalkMsg;
    public void run(CitySetting task) {
        System.out.println("执行任务: " + task.getId() + "，参数: " + task.getCityName());

        // 查询当地天气
        Location location = new Location();
        location.setLat(task.getLat());
        location.setLon(task.getLon());
        List<HourlyData> hourlyData = weatherService.queryWeatherFor24WithLocation(location);
        Assert.notEmpty(hourlyData, "天气API异常");

        // 仅开启下雨提醒
        boolean onlyRain = task.getRainNoticeOnly() == 1;
        boolean hasRain = hourlyData.stream().anyMatch(d -> d.getText().contains("雨"));
        if (onlyRain && hasRain) {
            System.out.println("发送消息：下雨提醒");
        } else if (!onlyRain) {
            dingTalkMsg.sendDingTalkMsg(task, hourlyData.get(0));
            System.out.println("发送消息：今日天气");
        }

    }

}
