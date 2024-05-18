package cn.qianzhikang.RestResponse;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Description 请求状态码
 * @Date 2022-11-05-14-54
 * @Author qianzhikang
 */

@AllArgsConstructor
@NoArgsConstructor
public enum RestCode {
    SUCCESS(1,"成功返回"),
    ERROR(0,"错误");

    private Integer code;
    private String message;

    public void setCode(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return this.code;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}