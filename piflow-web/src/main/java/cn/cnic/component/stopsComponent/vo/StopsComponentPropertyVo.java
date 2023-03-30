package cn.cnic.component.stopsComponent.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 * 组件属性(leader端接收follow端数据使用)
 * @author leilei
 * @date 2022-08-02
 */
@Setter
@Getter
public class StopsComponentPropertyVo {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String displayName;
    private String description;
    private String defaultValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;
    private Long propertySort;
    private String example;
    private String language;
}
