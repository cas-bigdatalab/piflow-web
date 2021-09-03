package cn.cnic.component.dataSource.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataSourceProperty extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private DataSource dataSource;

    private String name;
    private String value;
    private String description;

}
