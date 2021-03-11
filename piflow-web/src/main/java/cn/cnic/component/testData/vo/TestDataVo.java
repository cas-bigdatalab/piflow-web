package cn.cnic.component.testData.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataVo {

    private String id;
    private String name;
    private String description;
    private Date crtDttm;
    private String crtDttmString;
    private Date lastUpdateDttm;
    private String lastUpdateDttmString;
    private List<TestDataSchemaVo> schemaVoList = new ArrayList<>();

}
