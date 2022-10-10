
package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * stop property
 */
@Getter
@Setter
public class Property extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Stops stops;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;
    private Boolean isSelect;
    private Boolean isLocked = false;
    private Long propertySort;
    private Boolean isOldData = false;
    private String example;

}
