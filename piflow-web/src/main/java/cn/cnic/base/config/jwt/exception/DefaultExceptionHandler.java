package cn.cnic.base.config.jwt.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.cnic.base.config.jwt.common.ResultJson;
import cn.cnic.common.Eunm.ResultCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理类
 * controller层异常无法捕获处理，需要自己处理
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class DefaultExceptionHandler {

    /**
     * 处理所有自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultJson handleCustomException(CustomException e){
        log.error(e.getResultJson().getMsg().toString());
        return e.getResultJson();
    }
    /**
     * 处理参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultJson handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getBindingResult().getFieldError().getField() + e.getBindingResult().getFieldError().getDefaultMessage());
        return ResultJson.failure(ResultCode.BAD_REQUEST, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
