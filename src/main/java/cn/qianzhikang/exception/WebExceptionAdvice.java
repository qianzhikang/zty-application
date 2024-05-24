package cn.qianzhikang.exception;


import cn.qianzhikang.RestResponse.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qianzhikang
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public RestResult handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return new RestResult<>().error(e.getMessage());
    }
}