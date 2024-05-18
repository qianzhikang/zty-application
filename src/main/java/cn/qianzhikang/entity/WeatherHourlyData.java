package cn.qianzhikang.entity;

import lombok.Data;

import java.util.List;

/**
 * @author qianzhikang
 */
@Data
public class WeatherHourlyData {
    private String code;
    private String updateTime;
    private String fxLink;
    private List<HourlyData> hourly;
    private Refer refer;
}
