package cn.cnic.component.stopsComponent.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.StopsHubState;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Setter
@Getter
public class StopsHub extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String mountId;
    private String jarName;
    private String jarUrl;
    private StopsHubState status;

}
