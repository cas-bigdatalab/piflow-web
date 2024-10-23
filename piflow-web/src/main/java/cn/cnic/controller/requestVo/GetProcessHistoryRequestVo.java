package cn.cnic.controller.requestVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProcessHistoryRequestVo {
    private Integer limit;
    private Integer page;
    private String keyword;//用于接收关键词查询的关键词
    private String createUser;
    private String state;


}
