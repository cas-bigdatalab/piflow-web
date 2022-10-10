package cn.cnic.component.stopsComponent.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.StopsHubState;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 */
@Setter
@Getter
public class StopsHub extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String mountId;
    private String jarName;
    private String jarUrl;
    private StopsHubState status;

}
