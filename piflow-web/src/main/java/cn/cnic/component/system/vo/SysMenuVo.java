package cn.cnic.component.system.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SysMenuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String menuName;
    private String menuUrl;
    private String menuParent;
    private String menuDescription;
    private Integer menuSort;
}
