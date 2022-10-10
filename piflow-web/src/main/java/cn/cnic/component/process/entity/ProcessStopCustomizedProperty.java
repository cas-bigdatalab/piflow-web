
package cn.cnic.component.process.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * stop property
 */
@Getter
@Setter
public class ProcessStopCustomizedProperty extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private ProcessStop processStop;
    private String name;
    private String customValue;
    private String description;
}
