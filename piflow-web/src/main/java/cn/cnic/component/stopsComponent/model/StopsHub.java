package cn.cnic.component.stopsComponent.model;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.StopsHubState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Stop component table
 */
@Getter
@Setter
@Entity
@Table(name = "STOPS_HUB")
public class StopsHub extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar mount id'")
    private String mountId;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar name'")
    private String jarName;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar url'")
    private String jarUrl;

    @Column(columnDefinition = "varchar(255) COMMENT 'StopsHue status'")
    @Enumerated(EnumType.STRING)
    private StopsHubState status;

}
