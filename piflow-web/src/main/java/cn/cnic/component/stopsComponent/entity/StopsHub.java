package cn.cnic.component.stopsComponent.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ComponentFileType;
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
    private String bundles;
    private Boolean isPublishing;
    private ComponentFileType type;        //component type:PYTHON/SCALA
    private String languageVersion;         //the language version that the component depends on

}
