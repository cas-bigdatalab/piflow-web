package cn.cnic.component.stopsComponent.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Getter
@Setter
@Entity
@Table(name = "FLOW_STOPS_TEMPLATE_MANAGE")
public class StopsComponentManage extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String bundle;

    private String stopsGroups;
    
    private Boolean isShow = true;

}
