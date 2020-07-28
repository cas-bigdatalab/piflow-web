package cn.cnic.component.stopsComponent.model;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.common.Eunm.StopsHueState;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stop component table
 *
 * @author Nature
 */
@Getter
@Setter
@Entity
@Table(name = "STOPS_HUB")
public class StopsHub extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar name'")
    private String jarName;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar url'")
    private String jarUrl;

    @Column(columnDefinition = "varchar(255) COMMENT 'StopsHue status'")
    @Enumerated(EnumType.STRING)
    private StopsHueState status;

}
