package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FlowPublishing extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String flowId;
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private String description;
    private Integer flowSort;

    List<FlowStopsPublishingProperty> properties;
}
