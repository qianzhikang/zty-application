package cn.qianzhikang.ding;

import cn.hutool.http.HttpUtil;
import cn.qianzhikang.cache.ConfigCache;
import cn.qianzhikang.entity.CitySetting;
import cn.qianzhikang.entity.HourlyData;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyong
 */
@Component
public class DingTalkMsg {

    public void sendDingTalkMsg(CitySetting task, HourlyData data) {
        String msg = getTemplate(task.getCityName(), data);
        HttpUtil.post(task.getDingHook(), msg);
    }


    protected String getTemplate(String cityName, HourlyData data) {
        String otherTemplate = ConfigCache.get("template.other");
        Map<String, String> values = new HashMap<>();
        values.put("CITY", cityName);
        values.put("WEATHER_CONDITION", "天气情况: " + data.getText() + ", 今日气温: " + data.getTemp());
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "%" + entry.getKey().toUpperCase() + "%";
            otherTemplate = otherTemplate.replace(placeholder, entry.getValue());
        }
        return otherTemplate;
    }





}
