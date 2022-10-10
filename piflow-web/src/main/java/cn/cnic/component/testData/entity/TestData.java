package cn.cnic.component.testData.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestData extends BaseModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private List<TestDataSchema> schemaList = new ArrayList<>();
    private List<TestDataSchemaValues> schemaValuesList = new ArrayList<>();
    

}
