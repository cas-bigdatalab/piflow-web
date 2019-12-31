
package com.nature.component.flow.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * stop property
 *
 * @author Nature
 */
@Getter
@Setter
public class StopsCustomizedPropertyVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
    private String name;
    private String customValue;
    private String description;
    private String stopId;
}
