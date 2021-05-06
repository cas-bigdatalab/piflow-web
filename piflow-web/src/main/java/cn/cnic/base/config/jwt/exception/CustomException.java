package cn.cnic.base.config.jwt.exception;

import cn.cnic.base.config.jwt.common.ResultJson;
import lombok.Getter;


@Getter
@SuppressWarnings("rawtypes")
public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    private ResultJson resultJson;

    public CustomException(ResultJson resultJson) {
        this.resultJson = resultJson;
    }
}
