package cn.qianzhikang.entity;

import lombok.Data;

/**
 * @author qianzhikang
 */
@Data
public class HourlyData {
    /** 预报时间*/
    private String fxTime;
    /** 温度*/
    private String temp;
    /** 天气状况的图标代码*/
    private String icon;
    /** 天气状况的文字描述*/
    private String text;
    /** 风向360角度*/
    private String wind360;
    /** 风向*/
    private String windDir;
    /** 风力等级*/
    private String windScale;
    /** 风速，公里/小时*/
    private String windSpeed;
    /** 相对湿度，百分比数值*/
    private String humidity;
    /** 当前小时累计降水量，默认单位：毫米*/
    private String pop;
    /** 逐小时预报降水概率，百分比数值*/
    private String precip;
    /** 大气压强，默认单位：百帕*/
    private String pressure;
    /** 云量，百分比数值。可能为空*/
    private String cloud;
    /** 露点温度。可能为空*/
    private String dew;
}
