package cn.cnic.component.sparkJar.model;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.SparkJarState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Stop component table
 *
 */
@Getter
@Setter
@Entity
@Table(name = "SPARK_JAR")
public class SparkJarComponent extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar mount id'")
    private String mountId;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar name'")
    private String jarName;

    @Column(columnDefinition = "varchar(1000) COMMENT 'jar url'")
    private String jarUrl;

    @Column(columnDefinition = "varchar(255) COMMENT 'Spark jar status'")
    @Enumerated(EnumType.STRING)
    private SparkJarState status;

}
