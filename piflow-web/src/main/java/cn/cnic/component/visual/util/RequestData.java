package cn.cnic.component.visual.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求参数类
 * author:hmh
 * date:2023-02-21
 */
@Data
@NoArgsConstructor
public class RequestData {
    //查询内容
    private String queryContent;
    //分页
    private int pageSize;
    private int pageNum;

    private String tableName;
    private String x;
    private String y;

    private String driverClass;
    private String url;
    private String userName;
    private String password;

}
