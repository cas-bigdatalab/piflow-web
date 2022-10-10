package cn.cnic.component.sparkJar.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.SparkJarState;
import lombok.Getter;
import lombok.Setter;

/**
 * Stop component table
 *
 */
@Setter
@Getter
public class SparkJarComponent extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String mountId;
    private String jarName;
    private String jarUrl;
    private SparkJarState status;

}
