package cn.cnic.component.dataSource.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DataSource extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String dataSourceType;
    private String dataSourceName;
    private String dataSourceDescription;
    private Boolean isTemplate = false;
    private List<Stops> stopsList = new ArrayList<>();
    private List<DataSourceProperty> dataSourcePropertyList = new ArrayList<>();
    private String stopsTemplateBundle;
    private StopsComponent stopsComponent;

}
