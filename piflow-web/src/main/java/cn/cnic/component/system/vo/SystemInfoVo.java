package cn.cnic.component.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SystemInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
//    private String name;
    private String description;
    private String instructionFileId;//系统说明书id
    private String instructionFileName;//系统说明书名
    private String userApplyFileId;//用户申请表id
    private String userApplyFileName;//用户申请表名

}
