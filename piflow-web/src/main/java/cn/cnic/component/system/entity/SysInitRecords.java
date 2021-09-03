package cn.cnic.component.system.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
public class SysInitRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Date initDate = new Date();
    private Boolean isSucceed = Boolean.TRUE;

}
