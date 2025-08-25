package cn.cnic.component.schedule.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {
    private int code;
    private String processId;
    private String errorMsg;
}
