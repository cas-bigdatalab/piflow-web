package cn.cnic.component.stopsComponent.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * Group name table
 */
@Setter
@Getter
public class StopsComponentGroup extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String groupName; // Group name

    // Group contains stop
    private List<StopsComponent> stopsComponentList = new ArrayList<StopsComponent>();
}
