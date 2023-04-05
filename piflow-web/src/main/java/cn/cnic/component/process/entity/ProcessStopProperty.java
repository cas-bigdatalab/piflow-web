package cn.cnic.component.process.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessStopProperty extends BaseModelUUIDNoCorpAgentId implements Comparable<ProcessStopProperty> {

    private static final long serialVersionUID = 1L;

    private ProcessStop processStop;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;

    //TODO not save in table recently
    private Long propertySort;

    @Override
    public int compareTo(ProcessStopProperty o) {
        return this.propertySort.compareTo(o.propertySort);
    }
}
