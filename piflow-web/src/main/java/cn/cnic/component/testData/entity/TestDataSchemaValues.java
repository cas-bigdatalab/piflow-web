package cn.cnic.component.testData.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataSchemaValues extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String fieldValue;
    private int dataRow;
    private TestData testData;
    private TestDataSchema testDataSchema;

}
