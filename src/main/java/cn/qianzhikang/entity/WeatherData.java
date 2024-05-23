package cn.qianzhikang.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author qianzhikang
 */
@Data
@Builder
public class WeatherData {
    /** 城市 */
    private String cityName;
    /** 小时数据 */
    private List<HourlyData> hourlyData;
}
