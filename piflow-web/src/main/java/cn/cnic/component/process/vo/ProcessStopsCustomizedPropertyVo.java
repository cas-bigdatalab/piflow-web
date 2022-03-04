
package cn.cnic.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * stop property
 */
@Getter
@Setter
public class ProcessStopsCustomizedPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String customValue;
    private String description;
    private String stopId;
}
