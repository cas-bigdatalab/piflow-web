
package com.nature.component.flow.vo;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.flow.model.Stops;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * stop property
 *
 * @author Nature
 */
@Getter
@Setter
public class StopsCustomizedPropertyVo implements Serializable {

    private String id;
    private String name;
    private String customValue;
    private String description;
    private String stopId;
}
