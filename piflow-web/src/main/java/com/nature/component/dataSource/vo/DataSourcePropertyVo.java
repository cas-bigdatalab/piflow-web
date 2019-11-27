package com.nature.component.dataSource.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DataSourcePropertyVo implements Serializable {

    private String id;
    private String name;
    private String value;
    private String description;

}
