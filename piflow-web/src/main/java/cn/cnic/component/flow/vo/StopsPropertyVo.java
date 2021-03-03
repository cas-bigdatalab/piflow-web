
package cn.cnic.component.flow.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Stop attribute
 */

@Setter
@Getter
public class StopsPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private StopsVo stopsVo;

    private String name;

    private String displayName;

    private String description;

    private String customValue;

    private String allowableValues;

    private Boolean required;

    private Boolean sensitive;

    private Boolean isSelect;

    private Boolean isLocked = false;

    private String example;

    private String language;
}
