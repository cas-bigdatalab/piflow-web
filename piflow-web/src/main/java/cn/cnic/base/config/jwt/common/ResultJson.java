package cn.cnic.base.config.jwt.common;

import cn.cnic.common.Eunm.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * RESTful API 返回类型
 */
@Getter
@Setter
@SuppressWarnings("rawtypes")
public class ResultJson<T> implements Serializable{

    private static final long serialVersionUID = 783015033603078674L;
    private int code;
    private String msg;
    private T data;

    public static ResultJson ok() {
        return ok("");
    }

    @SuppressWarnings("unchecked")
    public static ResultJson ok(Object o) {
        return new ResultJson(ResultCode.SUCCESS, o);
    }

    public static ResultJson failure(ResultCode code) {
        return failure(code, "");
    }

    @SuppressWarnings("unchecked")
    public static ResultJson failure(ResultCode code, Object o) {
        return new ResultJson(code, o);
    }

    public ResultJson(ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson(ResultCode resultCode, T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}
