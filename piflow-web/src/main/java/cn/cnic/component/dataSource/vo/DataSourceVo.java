package cn.cnic.component.dataSource.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DataSourceVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String dataSourceType;
    private String dataSourceName;
    private String dataSourceDescription;
    private Boolean isTemplate = false;

    private List<DataSourcePropertyVo> dataSourcePropertyVoList = new ArrayList<>();

}
