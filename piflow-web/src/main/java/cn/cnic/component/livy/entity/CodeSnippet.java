package cn.cnic.component.livy.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CodeSnippet extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String codeContent;


}
