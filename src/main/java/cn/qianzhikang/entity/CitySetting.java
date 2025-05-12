package cn.qianzhikang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_city_setting
 */
@TableName(value ="tb_city_setting")
@Data
public class CitySetting implements Serializable {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 地区
     */
    private String cityName;

    /**
     * 经度
     */
    private String lon;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 0禁用，1启用
     */
    private Integer status;

    /**
     * 仅有雨时提示 0关闭，1开启
     */
    private Integer rainNoticeOnly;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 记录更新时间
     */
    private Date lastModifiedTime;


    /**
     * cron
     */
    private String cron;

    /**
     * 钉钉机器人hook值
     */
    private String dingHook;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}