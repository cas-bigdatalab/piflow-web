package cn.cnic.component.system.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {

    private String id;
    private String loginIp;
    private String loginUser;
    private Date loginTime;
}
