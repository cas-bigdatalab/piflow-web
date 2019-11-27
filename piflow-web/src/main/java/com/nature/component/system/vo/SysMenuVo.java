package com.nature.component.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SysMenuVo implements Serializable {

    private String menuName;
    private String menuUrl;
    private String menuParent;
    private String menuDescription;
    private Integer menuSort;
}
