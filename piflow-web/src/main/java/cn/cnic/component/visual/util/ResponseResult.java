package cn.cnic.component.visual.util;

import lombok.Data;

/**
 * 响应数据简单封装
 * author:hmh
 * date:2023-02-21
 */
@Data
public class ResponseResult <T> {
    private int code;       // 状态码
    private String msg;     // 返回的信息
    private T data;         // 返回的数据
    private Integer totalCount; //分页中用到的记录总条数

    private ResponseResult(T data){
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    private ResponseResult(T data, Integer totalCount){
        this.code = 200;
        this.msg = "success";
        this.data = data;
        this.totalCount = totalCount;
    }

    private ResponseResult() {
        this.code = 200;
        this.msg = "success";
    }

    private ResponseResult(String msg) {
        this.code = 400;
        this.msg = msg;
    }

    /**
     * @param data 返回的数据
     * @param <T> 返回的数据类型
     * @return 返回成功结果，携带数据（封装后）
     */
    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<T>(data);
    }

    /**
     *
     * @param data 返回的数据
     * @param totalCount 返回的记录总条数
     * @param <T> 返回的数据类型
     * @return 返回成功结果，携带数据（封装后）
     */
    public static <T> ResponseResult<T> success(T data, Integer totalCount){
        return new ResponseResult<T>(data,totalCount);
    }

    /**
     * @param <T> 返回的数据类型
     * @return 返回成功结果（封装后）
     */
    public static <T> ResponseResult<T> success(){
        return new ResponseResult<T>();
    }

    /**
     * @param msg 失败信息
     * @param <T> 返回的数据类型
     * @return 返回失败结果，携带错误信息（封装后）
     */
    public static <T> ResponseResult<T> error(String msg){
        return new ResponseResult<T>(msg);
    }

    /**
     * @param <T> 返回的数据类型
     * @return 返回失败结果（封装后）
     */
    public static <T> ResponseResult<T> error(){
        return new ResponseResult<T>("error");
    }
}
