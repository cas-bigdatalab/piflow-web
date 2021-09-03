package cn.cnic.component.stopsComponent.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Getter
@Setter
public class StopsComponentManage extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String bundle;

    private String stopsGroups;
    
    private Boolean isShow = true;

}
