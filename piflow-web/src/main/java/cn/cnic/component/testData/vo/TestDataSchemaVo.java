package cn.cnic.component.testData.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataSchemaVo {

    private String id;
    private String fieldName;
    private String fieldType;
    private String fieldDescription;
    private int fieldSoft;
    private TestDataVo testDataVo;
    private List<TestDataSchemaValuesVo> schemaValuesVoList = new ArrayList<>();


}
