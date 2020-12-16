package cn.cnic.component.testData.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataSchema extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String fieldType;
    private String fieldDescription;
    private int fieldSoft;


}
