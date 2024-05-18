package cn.qianzhikang.entity;

import lombok.Data;

/**
 * @author qianzhikang
 */
@Data
public class HourlyData {
    private String fxTime;
    private String temp;
    private String icon;
    private String text;
    private String wind360;
    private String windDir;
    private String windScale;
    private String windSpeed;
    private String humidity;
    private String pop;
    private String precip;
    private String pressure;
    private String cloud;
    private String dew;
}
