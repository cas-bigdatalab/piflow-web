package cn.cnic.component.system.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;

}
