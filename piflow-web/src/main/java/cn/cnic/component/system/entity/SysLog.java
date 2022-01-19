package cn.cnic.component.system.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_operation_log
 */

@Getter
@Setter
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String lastLoginIp;
    private String action;
    private Boolean status;
    private String result;
    private String comment;
    private Date ctrDttm;
    private Date lastUpdateDttm;
    private Boolean enableFlag;



}
