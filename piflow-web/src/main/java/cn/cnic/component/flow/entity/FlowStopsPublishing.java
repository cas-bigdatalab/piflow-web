package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * stop component table
 */
@Getter
@Setter
public class FlowStopsPublishing extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String publishingId;
    private String name;
    private String state;
    private List<String> stopsIds;
}
