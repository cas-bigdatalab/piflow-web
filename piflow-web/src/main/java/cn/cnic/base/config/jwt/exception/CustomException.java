package cn.cnic.base.config.jwt.exception;

import cn.cnic.base.config.jwt.common.ResultJson;
import lombok.Getter;

/**
 * @author Nature
 * Created at 2020/6/30.
 */
@Getter
public class CustomException extends RuntimeException{

    private ResultJson resultJson;

    public CustomException(ResultJson resultJson) {
        this.resultJson = resultJson;
    }
}
