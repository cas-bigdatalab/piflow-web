package cn.cnic.component.dataSource.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DATA_SOURCE_PROPERTY")
@Setter
@Getter
public class DataSourceProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_DATA_SOURCE_ID")
    private DataSource dataSource;

    @Column(columnDefinition = "varchar(255) COMMENT 'name'")
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT 'value'")
    private String value;
    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String description;

}
