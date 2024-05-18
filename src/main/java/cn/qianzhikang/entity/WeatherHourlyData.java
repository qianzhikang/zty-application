package cn.qianzhikang.entity;

import lombok.Data;

import java.util.List;

/**
 * @author qianzhikang
 */
@Data
public class WeatherHourlyData {
    /** 状态码 */
    private String code;
    /** 当前API的最近更新时间 */
    private String updateTime;
    /** 当前数据的响应式页面，便于嵌入网站或应用*/
    private String fxLink;
    /** 小时数据 */
    private List<HourlyData> hourly;
    /** 原始数据来源 */
    private Refer refer;
}
