package cn.qianzhikang.entity;

import lombok.Data;

/**
 * @author qianzhikang
 */
@Data
public class Location {
    // 城市名称
    private String name;

    // 城市ID
    private String id;

    // 纬度
    private String lat;

    // 经度
    private String lon;

    // 二级行政区划（如苏州市）
    private String adm2;

    // 一级行政区划（如江苏省）
    private String adm1;

    // 国家
    private String country;

    // 时区
    private String tz;

    // UTC偏移
    private String utcOffset;

    // 是否是夏令时（0表示否，1表示是）
    private String isDst;

    // 类型（如城市）
    private String type;

    // 排名
    private String rank;

    // 气象链接
    private String fxLink;
}
