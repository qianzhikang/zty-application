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
 * @TableName user_scheduled_tasks
 */
@TableName(value ="user_scheduled_tasks")
@Data
public class UserScheduledTasks implements Serializable {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 电子邮箱
     */
    private Integer email;

    /**
     * 任务类型：0-每日任务，1-间隔任务
     */
    private Integer taskType;

    /**
     * 每日任务使用
     */
    private Date startTime;

    /**
     * 间隔小时数
     */
    private Integer intervalHours;

    /**
     * 间隔任务使用，计算得到
     */
    private Date nextRunTime;

    /**
     * 
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 记录更新时间
     */
    private Date lastModifiedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}